package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransaction.Type;
import com.aixohub.portfolio.model.Client;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DividendsAndInterestCheck implements Check {
    @Override
    public List<Issue> execute(Client client) {
        List<Issue> answer = new ArrayList<Issue>();

        for (Account account : client.getAccounts()) {
            for (AccountTransaction transaction : account.getTransactions()) {
                if (transaction.getType() == Type.DIVIDENDS && transaction.getSecurity() == null) {
                    answer.add(new DividendsAndInterestIssue(client, account, transaction, Type.INTEREST));
                } else if (transaction.getType() == Type.INTEREST && transaction.getSecurity() != null) {
                    answer.add(new DividendsAndInterestIssue(client, account, transaction, Type.DIVIDENDS));
                }
            }
        }

        return answer;
    }

    private static final class DividendsAndInterestIssue extends AbstractAccountIssue {
        private final Type target;

        public DividendsAndInterestIssue(Client client, Account account, AccountTransaction transaction, Type target) {
            super(client, account, transaction);
            this.target = target;
        }

        @Override
        public String getLabel() {
            if (target == Type.INTEREST)
                return Messages.IssueDividendWithoutSecurity;
            else
                return MessageFormat.format(Messages.IssueInterestWithSecurity, transaction.getSecurity().getName());
        }

        @Override
        public List<QuickFix> getAvailableFixes() {
            List<QuickFix> answer = new ArrayList<QuickFix>();
            answer.add(new ConvertFix(transaction, target));
            answer.add(new DeleteTransactionFix<AccountTransaction>(client, account, transaction));
            return answer;
        }
    }

    private static final class ConvertFix implements QuickFix {
        private final AccountTransaction transaction;
        private final Type target;

        public ConvertFix(AccountTransaction transaction, Type target) {
            this.transaction = transaction;
            this.target = target;
        }

        @Override
        public String getLabel() {
            return MessageFormat.format(Messages.FixConvertToType, target);
        }

        @Override
        public void execute() {
            transaction.setType(target);
        }

        @Override
        public String getDoneLabel() {
            return MessageFormat.format(Messages.FixConvertToTypeDone, target);
        }
    }
}
