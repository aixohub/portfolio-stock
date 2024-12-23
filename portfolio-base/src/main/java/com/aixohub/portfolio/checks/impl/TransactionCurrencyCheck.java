package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransferEntry;
import com.aixohub.portfolio.model.BuySellEntry;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.PortfolioTransferEntry;
import com.aixohub.portfolio.model.Transaction;
import com.aixohub.portfolio.model.TransactionOwner;
import com.aixohub.portfolio.model.TransactionPair;
import com.aixohub.portfolio.money.CurrencyUnit;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Checks if there is at least one account or security without a currency.
 */
public class TransactionCurrencyCheck implements Check {
    @Override
    public List<Issue> execute(Client client) {
        Set<Object> transactions = new HashSet<Object>();

        for (Account account : client.getAccounts()) {
            account.getTransactions()
                    .stream()
                    .filter(t -> t.getCurrencyCode() == null)
                    .forEach(t -> transactions.add(t.getCrossEntry() != null ? t.getCrossEntry()
                            : new TransactionPair<AccountTransaction>(account, t)));
        }

        for (Portfolio portfolio : client.getPortfolios()) {
            portfolio.getTransactions()
                    .stream()
                    .filter(t -> t.getCurrencyCode() == null)
                    .forEach(t -> transactions.add(t.getCrossEntry() != null ? t.getCrossEntry()
                            : new TransactionPair<PortfolioTransaction>(portfolio, t)));
        }

        List<Issue> issues = new ArrayList<Issue>();

        for (Object t : transactions) {
            if (t instanceof TransactionPair<?>) {
                @SuppressWarnings("unchecked")
                TransactionPair<Transaction> pair = (TransactionPair<Transaction>) t;
                issues.add(new TransactionMissingCurrencyIssue(client, pair));
            } else if (t instanceof BuySellEntry entry) {
                // attempt to fix it if both currencies are identical. If a fix
                // involves currency conversion plus exchange rates, just offer
                // to delete the transaction.

                String accountCurrency = entry.getAccount().getCurrencyCode();
                String securityCurrency = entry.getPortfolioTransaction().getSecurity().getCurrencyCode();

                @SuppressWarnings("unchecked")
                TransactionPair<Transaction> pair = new TransactionPair<Transaction>(
                        (TransactionOwner<Transaction>) entry.getOwner(entry.getAccountTransaction()),
                        entry.getAccountTransaction());
                issues.add(new TransactionMissingCurrencyIssue(client, pair, Objects.equals(accountCurrency,
                        securityCurrency)));
            } else if (t instanceof AccountTransferEntry entry) {
                // same story as with purchases: only offer to fix if currencies
                // match

                String sourceCurrency = entry.getSourceAccount().getCurrencyCode();
                String targetCurrency = entry.getTargetAccount().getCurrencyCode();

                @SuppressWarnings("unchecked")
                TransactionPair<Transaction> pair = new TransactionPair<Transaction>(
                        (TransactionOwner<Transaction>) entry.getOwner(entry.getSourceTransaction()),
                        entry.getSourceTransaction());
                issues.add(new TransactionMissingCurrencyIssue(client, pair, Objects.equals(sourceCurrency,
                        targetCurrency)));
            } else if (t instanceof PortfolioTransferEntry entry) {
                // transferring a security involves no currency change because
                // the currency is defined the security itself

                @SuppressWarnings("unchecked")
                TransactionPair<Transaction> pair = new TransactionPair<Transaction>(
                        (TransactionOwner<Transaction>) entry.getOwner(entry.getSourceTransaction()),
                        entry.getSourceTransaction());
                issues.add(new TransactionMissingCurrencyIssue(client, pair));
            } else {
                throw new IllegalArgumentException(
                        "unsupported transaction entry " + t.getClass() + ": " + t); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        return issues;
    }

    public static class TransactionCurrencyQuickFix implements QuickFix {
        private final TransactionPair<?> pair;
        private final String currencyCode;

        public TransactionCurrencyQuickFix(Client client, TransactionPair<?> pair) {
            this.pair = pair;

            // either take currency from account or from security. Use base
            // currency as a fallback
            this.currencyCode = pair.getOwner() instanceof Account ? ((Account) pair.getOwner()).getCurrencyCode()
                    : (pair.getOwner() instanceof Portfolio ? pair.getTransaction()
                    .getSecurity().getCurrencyCode() : client.getBaseCurrency());
        }

        @Override
        public String getLabel() {
            return CurrencyUnit.getInstance(currencyCode).getLabel();
        }

        @Override
        public String getDoneLabel() {
            return MessageFormat.format(Messages.FixAssignCurrencyCodeDone, currencyCode);
        }

        @Override
        public void execute() {
            pair.getTransaction().setCurrencyCode(currencyCode);

            // since currency fixes are only created if the currency is
            // identical, we can safely set the currency on both transactions
            if (pair.getTransaction().getCrossEntry() != null) {
                pair.getTransaction().getCrossEntry().getCrossTransaction(pair.getTransaction())
                        .setCurrencyCode(currencyCode);
            }
        }
    }

    private static class TransactionMissingCurrencyIssue implements Issue {
        private final Client client;
        private final TransactionPair<Transaction> pair;
        private final boolean isFixable;

        public TransactionMissingCurrencyIssue(Client client, TransactionPair<Transaction> pair) {
            this(client, pair, true);
        }

        public TransactionMissingCurrencyIssue(Client client, TransactionPair<Transaction> pair, boolean isFixable) {
            this.client = client;
            this.pair = pair;
            this.isFixable = isFixable;
        }

        @Override
        public LocalDate getDate() {
            return pair.getTransaction().getDateTime().toLocalDate();
        }

        @Override
        public Object getEntity() {
            return pair.getOwner();
        }

        @Override
        public Long getAmount() {
            return pair.getTransaction().getAmount();
        }

        @Override
        public String getLabel() {
            String type = pair.getTransaction() instanceof AccountTransaction at ? at.getType().toString()
                    : ((PortfolioTransaction) pair.getTransaction())
                    .getType().toString();
            return MessageFormat.format(Messages.IssueTransactionMissingCurrencyCode, type);
        }

        @Override
        public List<QuickFix> getAvailableFixes() {
            List<QuickFix> fixes = new ArrayList<QuickFix>();

            fixes.add(new DeleteTransactionFix<Transaction>(client, pair.getOwner(), pair.getTransaction()));
            if (isFixable)
                fixes.add(new TransactionCurrencyQuickFix(client, pair));

            return fixes;
        }
    }
}
