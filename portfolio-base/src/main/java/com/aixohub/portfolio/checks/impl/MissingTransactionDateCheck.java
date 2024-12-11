package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.Transaction;
import com.aixohub.portfolio.model.TransactionOwner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MissingTransactionDateCheck implements Check {
    @Override
    public List<Issue> execute(Client client) {
        var answer = new ArrayList<Issue>();

        for (Account account : client.getAccounts()) {
            for (AccountTransaction tx : account.getTransactions()) {
                if (tx.getDateTime() == null) {
                    answer.add(new MissingDateIssue<>(account, tx));
                }
            }
        }

        for (Portfolio portfolio : client.getPortfolios()) {
            for (PortfolioTransaction tx : portfolio.getTransactions()) {
                if (tx.getDateTime() == null) {
                    answer.add(new MissingDateIssue<>(portfolio, tx));
                }
            }
        }

        return answer;
    }

    public class MissingDateFix implements QuickFix {
        private final Transaction tx;

        public MissingDateFix(Transaction tx) {
            this.tx = tx;
        }

        @Override
        public String getLabel() {
            return Messages.FixSetDateToToday;
        }

        @Override
        public String getDoneLabel() {
            return Messages.FixSetDateToTodayDone;
        }

        @Override
        public void execute() {
            tx.setDateTime(LocalDate.now().atStartOfDay());
        }
    }

    private final class MissingDateIssue<T extends Transaction> implements Issue {
        private final TransactionOwner<T> owner;
        private final T tx;

        public MissingDateIssue(TransactionOwner<T> owner, T tx) {
            this.owner = owner;
            this.tx = tx;
        }

        @Override
        public LocalDate getDate() {
            // if the transaction has been fixed, the date should be
            return tx.getDateTime() != null ? tx.getDateTime().toLocalDate() : null;
        }

        @Override
        public Object getEntity() {
            return owner;
        }

        @Override
        public Long getAmount() {
            return tx.getAmount();
        }

        @Override
        public String getLabel() {
            return Messages.IssueTransactionWithoutDate;
        }

        @Override
        public List<QuickFix> getAvailableFixes() {
            List<QuickFix> fixes = new ArrayList<>();
            fixes.add(new MissingDateFix(tx));
            return fixes;
        }
    }
}
