package com.aixohub.portfolio.snapshot.filter;

import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.PortfolioTransaction.Type;
import com.aixohub.portfolio.model.PortfolioTransferEntry;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.Transaction.Unit;
import com.aixohub.portfolio.model.TransactionPair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Creates a Client that does only contain the specified securities and their
 * related transactions (buy, sell, deliveries, dividends, taxes, fees).
 */
public class ClientSecurityFilter implements ClientFilter {
    private final List<Security> securities;

    public ClientSecurityFilter(Security... securities) {
        this.securities = Arrays.asList(securities);
    }

    @Override
    public Client filter(Client client) {
        ReadOnlyClient pseudoClient = new ReadOnlyClient(client);

        Map<Account, ReadOnlyAccount> account2readonly = new HashMap<>();
        Map<Portfolio, ReadOnlyPortfolio> portfolio2readonly = new HashMap<>();

        Function<Account, ReadOnlyAccount> transformAccount = a -> account2readonly.computeIfAbsent(a, aa -> {
            ReadOnlyAccount readonly = new ReadOnlyAccount(aa);
            pseudoClient.internalAddAccount(readonly);
            return readonly;
        });

        Function<Portfolio, ReadOnlyPortfolio> transformPortfolio = p -> portfolio2readonly.computeIfAbsent(p, pp -> {
            ReadOnlyPortfolio pseudoPortfolio = new ReadOnlyPortfolio(pp);
            pseudoPortfolio.setReferenceAccount(transformAccount.apply(pp.getReferenceAccount()));
            pseudoClient.internalAddPortfolio(pseudoPortfolio);
            return pseudoPortfolio;
        });

        for (Security security : securities) {
            pseudoClient.internalAddSecurity(security);
            addSecurity(client, transformPortfolio, transformAccount, security);
        }

        return pseudoClient;
    }

    @SuppressWarnings("unchecked")
    private void addSecurity(Client client, Function<Portfolio, ReadOnlyPortfolio> getPortfolio,
                             Function<Account, ReadOnlyAccount> getAccount, Security security) {
        List<TransactionPair<?>> transactions = security.getTransactions(client);

        for (TransactionPair<?> pair : transactions) {
            if (pair.getTransaction() instanceof PortfolioTransaction)
                addPortfolioTransaction(getPortfolio, (TransactionPair<PortfolioTransaction>) pair);
            else if (pair.getTransaction() instanceof AccountTransaction)
                addAccountTransaction(getAccount, (TransactionPair<AccountTransaction>) pair);
        }

    }

    private void addPortfolioTransaction(Function<Portfolio, ReadOnlyPortfolio> getPortfolio,
                                         TransactionPair<PortfolioTransaction> pair) {
        Type type = pair.getTransaction().getType();
        switch (type) {
            case BUY:
            case DELIVERY_INBOUND:
                getPortfolio.apply((Portfolio) pair.getOwner()).internalAddTransaction(
                        convertToDelivery(pair.getTransaction(), PortfolioTransaction.Type.DELIVERY_INBOUND));
                break;
            case SELL:
            case DELIVERY_OUTBOUND:
                getPortfolio.apply((Portfolio) pair.getOwner()).internalAddTransaction(
                        convertToDelivery(pair.getTransaction(), PortfolioTransaction.Type.DELIVERY_OUTBOUND));
                break;
            case TRANSFER_IN:
                convertTransfer(getPortfolio, pair);
            case TRANSFER_OUT:
                // handled via TRANSFER_IN
                break;
            default:
                throw new IllegalArgumentException("unsupported type " + type); //$NON-NLS-1$
        }
    }

    private void addAccountTransaction(Function<Account, ReadOnlyAccount> getAccount,
                                       TransactionPair<AccountTransaction> pair) {
        AccountTransaction t = pair.getTransaction();

        com.aixohub.portfolio.model.AccountTransaction.Type type = pair.getTransaction().getType();
        switch (type) {
            case DIVIDENDS:
                long taxes = t.getUnitSum(Unit.Type.TAX).getAmount();
                long amount = t.getAmount();

                AccountTransaction copy = new AccountTransaction(t.getDateTime(), t.getCurrencyCode(), amount + taxes,
                        t.getSecurity(), t.getType());

                t.getUnits().filter(u -> u.getType() != Unit.Type.TAX).forEach(copy::addUnit);

                getAccount.apply((Account) pair.getOwner()).internalAddTransaction(copy);
                getAccount.apply((Account) pair.getOwner())
                        .internalAddTransaction(new AccountTransaction(t.getDateTime(), t.getCurrencyCode(),
                                amount + taxes, null, AccountTransaction.Type.REMOVAL));
                break;
            case FEES:
                getAccount.apply((Account) pair.getOwner()).internalAddTransaction(t);
                getAccount.apply((Account) pair.getOwner())
                        .internalAddTransaction(new AccountTransaction(t.getDateTime(), t.getCurrencyCode(),
                                t.getAmount(), null, AccountTransaction.Type.DEPOSIT));
                break;
            case FEES_REFUND:
                getAccount.apply((Account) pair.getOwner()).internalAddTransaction(t);
                getAccount.apply((Account) pair.getOwner())
                        .internalAddTransaction(new AccountTransaction(t.getDateTime(), t.getCurrencyCode(),
                                t.getAmount(), null, AccountTransaction.Type.REMOVAL));
                break;
            case TAXES:
            case TAX_REFUND:
                // ignore taxes
                break;
            case BUY:
            case SELL:
            case DEPOSIT:
            case REMOVAL:
            case TRANSFER_IN:
            case TRANSFER_OUT:
            case INTEREST:
            case INTEREST_CHARGE:
            default:
                throw new IllegalArgumentException("unsupported type " + type); //$NON-NLS-1$
        }
    }

    private PortfolioTransaction convertToDelivery(PortfolioTransaction t, PortfolioTransaction.Type targetType) {
        PortfolioTransaction pseudo = new PortfolioTransaction();
        pseudo.setDateTime(t.getDateTime());
        pseudo.setCurrencyCode(t.getCurrencyCode());
        pseudo.setSecurity(t.getSecurity());
        pseudo.setShares(t.getShares());
        pseudo.setType(targetType);

        // calculation is without taxes -> remove any taxes & adapt
        // total accordingly

        long taxes = t.getUnitSum(Unit.Type.TAX).getAmount();
        long amount = t.getAmount();

        pseudo.setAmount(pseudo.getType() == PortfolioTransaction.Type.DELIVERY_INBOUND ? amount - taxes
                : amount + taxes);

        // copy all units (except for taxes) over to the pseudo
        // transaction
        t.getUnits().filter(u -> u.getType() != Unit.Type.TAX).forEach(pseudo::addUnit);

        return pseudo;
    }

    private void convertTransfer(Function<Portfolio, ReadOnlyPortfolio> getPortfolio,
                                 TransactionPair<PortfolioTransaction> pair) {
        PortfolioTransferEntry entry = (PortfolioTransferEntry) pair.getTransaction().getCrossEntry();

        ReadOnlyPortfolio source = getPortfolio.apply(entry.getSourcePortfolio());
        ReadOnlyPortfolio target = getPortfolio.apply(entry.getTargetPortfolio());

        ClientFilterHelper.recreateTransfer(entry, source, target);
    }
}
