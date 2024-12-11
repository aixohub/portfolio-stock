package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransferEntry;
import com.aixohub.portfolio.model.Client;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/* package */class MissingAccountTransferIssue extends AbstractAccountIssue {
    public MissingAccountTransferIssue(Client client, Account account, AccountTransaction transaction) {
        super(client, account, transaction);
    }

    @Override
    public String getLabel() {
        return MessageFormat.format(Messages.IssueMissingAccountTransfer, transaction.getType().toString());
    }

    @Override
    public List<QuickFix> getAvailableFixes() {
        List<QuickFix> answer = client.getAccounts().stream() //
                .filter(a -> !a.equals(account)) //
                .map(a -> new CreateTransferFix(a)) //
                .collect(Collectors.toList());
        answer.add(new DeleteTransactionFix<AccountTransaction>(client, account, transaction));
        return answer;
    }

    private final class CreateTransferFix implements QuickFix {
        private final Account crossAccount;

        private CreateTransferFix(Account crossAccount) {
            this.crossAccount = crossAccount;
        }

        @Override
        public String getLabel() {
            return MessageFormat.format(Messages.FixCreateTransfer, crossAccount.getName());
        }

        @Override
        public String getDoneLabel() {
            AccountTransaction.Type target = null;
            if (transaction.getType() == AccountTransaction.Type.TRANSFER_IN)
                target = AccountTransaction.Type.TRANSFER_OUT;
            else
                target = AccountTransaction.Type.TRANSFER_IN;

            return MessageFormat.format(Messages.FixCreateTransferDone, target.toString());
        }

        @Override
        public void execute() {
            Account from = null;
            Account to = null;

            if (transaction.getType() == AccountTransaction.Type.TRANSFER_IN) {
                from = crossAccount;
                to = account;
            } else {
                from = account;
                to = crossAccount;
            }

            AccountTransferEntry entry = new AccountTransferEntry(from, to);
            entry.setDate(transaction.getDateTime());
            entry.setAmount(transaction.getAmount());
            entry.setCurrencyCode(transaction.getCurrencyCode());
            entry.insert();

            account.getTransactions().remove(transaction);
        }
    }

}
