package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ReferenceAccountCheck implements Check {
    @Override
    public List<Issue> execute(Client client) {
        List<Issue> answer = new ArrayList<Issue>();

        for (Portfolio portfolio : client.getPortfolios()) {
            if (portfolio.getReferenceAccount() == null)
                answer.add(new MissingReferenceAccountIssue(client, portfolio));
        }

        return answer;
    }

    private static class MissingReferenceAccountIssue extends AbstractPortfolioIssue {
        public MissingReferenceAccountIssue(Client client, Portfolio portfolio) {
            super(client, portfolio, null);
        }

        @Override
        public String getLabel() {
            return Messages.IssueMissingReferenceAccount;
        }

        @Override
        public List<QuickFix> getAvailableFixes() {
            List<QuickFix> answer = new ArrayList<QuickFix>();
            answer.add(new CreateReferenceAccountFix(client, portfolio));
            for (Account account : client.getAccounts())
                answer.add(new AssignExistingAccountFix(portfolio, account));
            return answer;
        }
    }

    private static class AssignExistingAccountFix implements QuickFix {
        private final Portfolio portfolio;
        private final Account account;

        public AssignExistingAccountFix(Portfolio portfolio, Account account) {
            this.portfolio = portfolio;
            this.account = account;
        }

        @Override
        public String getLabel() {
            return MessageFormat.format(Messages.FixAssignExistingAccount, account.getName());
        }

        @Override
        public String getDoneLabel() {
            return MessageFormat.format(Messages.FixAssignExistingAccountDone, account.getName());
        }

        @Override
        public void execute() {
            portfolio.setReferenceAccount(account);
        }
    }

    private static class CreateReferenceAccountFix implements QuickFix {
        private final Client client;
        private final Portfolio portfolio;
        private final String nameProposal;

        public CreateReferenceAccountFix(Client client, Portfolio portfolio) {
            this.client = client;
            this.portfolio = portfolio;
            this.nameProposal = MessageFormat.format(Messages.FixReferenceAccountNameProposal, portfolio.getName());
        }

        @Override
        public String getLabel() {
            return MessageFormat.format(Messages.FixCreateReferenceAccount, nameProposal);
        }

        @Override
        public String getDoneLabel() {
            return MessageFormat.format(Messages.FixCreateReferenceAccountDone, nameProposal);
        }

        @Override
        public void execute() {
            Account account = new Account();
            account.setName(nameProposal);
            client.addAccount(account);
            portfolio.setReferenceAccount(account);
        }
    }

}
