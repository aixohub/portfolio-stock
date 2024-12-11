package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.Security;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PortfolioTransactionWithoutSecurityCheck implements Check {
    @Override
    public List<Issue> execute(Client client) {
        List<Issue> issues = new ArrayList<Issue>();

        for (Portfolio portfolio : client.getPortfolios()) {
            portfolio.getTransactions().stream() //
                    .filter(t -> t.getSecurity() == null) //
                    .forEach(t -> issues.add(new MissingSecurityIssue(client, portfolio, t)));

        }

        return issues;
    }

    public static class MissingSecurityIssue implements Issue {
        private final Client client;
        private final Portfolio portfolio;
        private final PortfolioTransaction transaction;

        public MissingSecurityIssue(Client client, Portfolio portfolio, PortfolioTransaction transaction) {
            this.client = client;
            this.portfolio = portfolio;
            this.transaction = transaction;
        }

        @Override
        public LocalDate getDate() {
            return transaction.getDateTime().toLocalDate();
        }

        @Override
        public Object getEntity() {
            return portfolio;
        }

        @Override
        public Long getAmount() {
            return transaction.getAmount();
        }

        @Override
        public String getLabel() {
            return Messages.IssuePortfolioTransactionWithoutSecurity;
        }

        @Override
        public List<QuickFix> getAvailableFixes() {
            List<QuickFix> fixes = new ArrayList<QuickFix>();

            fixes.add(new DeleteTransactionFix<PortfolioTransaction>(client, portfolio, transaction));

            for (Security security : client.getSecurities())
                fixes.add(new SetSecurityFix(security, transaction));

            return fixes;
        }
    }

    public static class SetSecurityFix implements QuickFix {
        private final Security security;
        private final PortfolioTransaction transaction;

        public SetSecurityFix(Security security, PortfolioTransaction transaction) {
            this.security = security;
            this.transaction = transaction;
        }

        @Override
        public String getLabel() {
            return MessageFormat.format(Messages.FixSetSecurity, security.getName());
        }

        @Override
        public String getDoneLabel() {
            return MessageFormat.format(Messages.FixSetSecurityDone, security.getName());
        }

        @Override
        public void execute() {
            transaction.setSecurity(security);
            if (transaction.getCrossEntry() != null)
                transaction.getCrossEntry().updateFrom(transaction);
        }
    }

}
