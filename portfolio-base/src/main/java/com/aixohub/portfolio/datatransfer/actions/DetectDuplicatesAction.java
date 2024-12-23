package com.aixohub.portfolio.datatransfer.actions;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.datatransfer.ImportAction;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransferEntry;
import com.aixohub.portfolio.model.BuySellEntry;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.InvestmentPlan;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.PortfolioTransferEntry;
import com.aixohub.portfolio.model.Transaction;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class DetectDuplicatesAction implements ImportAction {
    private final Client client;

    public DetectDuplicatesAction(Client client) {
        this.client = client;
    }

    @Override
    public Status process(AccountTransaction transaction, Account account) {
        return check(transaction, account.getTransactions());
    }

    @Override
    public Status process(PortfolioTransaction transaction, Portfolio portfolio) {
        return check(transaction, portfolio.getTransactions());
    }

    @Override
    public Status process(BuySellEntry entry, Account account, Portfolio portfolio) {
        // search for a match in existing investment plan transactions
        List<InvestmentPlan> plans = client.getPlans();
        Iterator<InvestmentPlan> i = plans.stream()
                .filter(p -> p.getSecurity() != null
                        && p.getSecurity().equals(entry.getPortfolioTransaction().getSecurity()))
                .iterator();
        while (i.hasNext()) {
            List<Transaction> transactions = i.next().getTransactions();
            for (Transaction t : transactions) {
                // use portfolio transaction, because it contains the number of
                // shares
                if (isInvestmentPlanDuplicate(entry.getPortfolioTransaction(), t))
                    return new Status(Status.Code.WARNING, Messages.InvestmentPlanItemImportToolTip);
            }
        }

        Status status = check(entry.getAccountTransaction(), account.getTransactions());
        if (status.getCode() != Status.Code.OK)
            return status;
        return check(entry.getPortfolioTransaction(), portfolio.getTransactions());
    }

    @Override
    public Status process(AccountTransferEntry entry, Account source, Account target) {
        return check(entry.getSourceTransaction(), source.getTransactions());
    }

    @Override
    public Status process(PortfolioTransferEntry entry, Portfolio source, Portfolio target) {
        return check(entry.getTargetTransaction(), source.getTransactions());
    }

    public Transaction findInvestmentPlanTransaction(Transaction subject, List<Transaction> transactions) {
        for (Transaction t : transactions) {
            // search investment plan transactions for potential duplicates
            if (isInvestmentPlanDuplicate(subject, t))
                return t;
        }
        return null;
    }

    private Status check(AccountTransaction subject, List<AccountTransaction> transactions) {
        for (AccountTransaction t : transactions) {
            if (subject.getType() != t.getType())
                continue;

            if (isPotentialDuplicate(subject, t))
                return new Status(Status.Code.WARNING, Messages.LabelPotentialDuplicate);
        }

        return Status.OK_STATUS;
    }

    private Status check(PortfolioTransaction subject, List<PortfolioTransaction> transactions) {
        Predicate<PortfolioTransaction> equivalentTypes;

        switch (subject.getType()) {
            case BUY:
            case DELIVERY_INBOUND:
                equivalentTypes = t -> t.getType() == PortfolioTransaction.Type.BUY
                        || t.getType() == PortfolioTransaction.Type.DELIVERY_INBOUND;
                break;
            case SELL:
            case DELIVERY_OUTBOUND:
                equivalentTypes = t -> t.getType() == PortfolioTransaction.Type.SELL
                        || t.getType() == PortfolioTransaction.Type.DELIVERY_OUTBOUND;
                break;
            default:
                equivalentTypes = t -> subject.getType() == t.getType();
        }

        for (PortfolioTransaction t : transactions) {
            if (!equivalentTypes.test(t))
                continue;

            if (isPotentialDuplicate(subject, t))
                return new Status(Status.Code.WARNING, Messages.LabelPotentialDuplicate);
        }

        return Status.OK_STATUS;
    }

    private boolean isPotentialDuplicate(Transaction subject, Transaction other) {
        if (!other.getDateTime().toLocalDate().equals(subject.getDateTime().toLocalDate()))
            return false;

        if (!other.getCurrencyCode().equals(subject.getCurrencyCode()))
            return false;

        if (other.getAmount() != subject.getAmount())
            return false;

        if (other.getShares() != subject.getShares())
            return false;

        // NOSONAR
        return Objects.equals(other.getSecurity(), subject.getSecurity());
    }

    /*
     * The other transaction is one generated by an investment plan, stored in
     * the client
     */
    private boolean isInvestmentPlanDuplicate(Transaction subject, Transaction other) {
        if (!other.getCurrencyCode().equals(subject.getCurrencyCode()))
            return false;

        if (!Objects.equals(other.getSecurity(), subject.getSecurity())) // NOSONAR
            return false;

        // amount might differ due to rounding - accept a difference of 1%
        long amount = subject.getAmount();
        if (amount * 1.01 < other.getAmount() || amount * 0.99 > other.getAmount())
            return false;

        LocalDateTime date = subject.getDateTime();
        // date can be up to five days after other's date (due to shifted
        // executions on weekends and holidays)
        if (date.isBefore(other.getDateTime()) || date.minusDays(5).isAfter(other.getDateTime()))
            return false;

        // number of shares might differ due to differing prices per share used
        // by investment plan
        // accept a difference of 10%
        long shares = subject.getShares();
        return !(shares * 1.1 < other.getShares()) && !(shares * 0.9 > other.getShares());
    }
}
