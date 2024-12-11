package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Client;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/* package */class BuySellMissingSecurityIssue extends AbstractAccountIssue {
    public BuySellMissingSecurityIssue(Client client, Account account, AccountTransaction transaction) {
        super(client, account, transaction);
    }

    @Override
    public String getLabel() {
        return MessageFormat.format(Messages.IssueBuySellWithoutSecurity, transaction.getType().toString());
    }

    @Override
    public List<QuickFix> getAvailableFixes() {
        List<QuickFix> answer = new ArrayList<QuickFix>();
        answer.add(new DeleteTransactionFix<AccountTransaction>(client, account, transaction));
        return answer;
    }
}
