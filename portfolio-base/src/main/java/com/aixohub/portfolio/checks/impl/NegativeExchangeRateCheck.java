package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.BuySellEntry;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.Transaction;
import com.aixohub.portfolio.model.TransactionPair;
import com.aixohub.portfolio.money.Values;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Checks if there are transaction units with negative exchange rates.
 */
public class NegativeExchangeRateCheck implements Check {
    @Override
    public List<Issue> execute(Client client) {
        List<Issue> issues = new ArrayList<>();

        for (Account account : client.getAccounts()) {
            for (AccountTransaction t : account.getTransactions()) // NOSONAR
            {
                if (t.getCrossEntry() instanceof BuySellEntry)
                    continue;

                if (t.getType() == AccountTransaction.Type.TRANSFER_IN)
                    continue;

                for (Transaction.Unit unit : t.getUnits().collect(Collectors.toList())) {
                    if (unit.getExchangeRate() != null && unit.getExchangeRate().signum() < 0) {
                        issues.add(new NegativeExchangeRateIssue(client, new TransactionPair<>(account, t),
                                MessageFormat.format(Messages.IssueExchangeRateIsNegative,
                                        Values.ExchangeRate.format(unit.getExchangeRate()),
                                        t.getType())));
                        break;
                    }
                }
            }
        }

        for (Portfolio portfolio : client.getPortfolios()) {
            for (PortfolioTransaction t : portfolio.getTransactions()) // NOSONAR
            {
                if (t.getType() == PortfolioTransaction.Type.TRANSFER_IN)
                    continue;

                for (Transaction.Unit unit : t.getUnits().collect(Collectors.toList())) {
                    if (unit.getExchangeRate() != null && unit.getExchangeRate().signum() < 0) {
                        issues.add(new NegativeExchangeRateIssue(client, new TransactionPair<>(portfolio, t),
                                MessageFormat.format(Messages.IssueExchangeRateIsNegative,
                                        Values.ExchangeRate.format(unit.getExchangeRate()),
                                        t.getType())));
                        break;
                    }
                }
            }
        }

        return issues;
    }

    private static class NegativeExchangeRateIssue implements Issue {
        private final Client client;
        private final TransactionPair<?> pair;
        private final String label;

        public NegativeExchangeRateIssue(Client client, TransactionPair<?> pair, String label) {
            this.client = client;
            this.pair = pair;
            this.label = label;
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
            return label;
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<QuickFix> getAvailableFixes() {
            List<QuickFix> fixes = new ArrayList<>();

            fixes.add(new DeleteTransactionFix<Transaction>(client, (TransactionPair<Transaction>) pair));

            return fixes;
        }
    }
}
