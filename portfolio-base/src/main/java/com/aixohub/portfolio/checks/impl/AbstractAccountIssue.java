package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Client;

import java.time.LocalDate;

/* package */abstract class AbstractAccountIssue implements Issue {
    protected Client client;
    protected Account account;
    protected AccountTransaction transaction;

    public AbstractAccountIssue(Client client, Account account, AccountTransaction transaction) {
        this.client = client;
        this.account = account;
        this.transaction = transaction;
    }

    @Override
    public LocalDate getDate() {
        return transaction.getDateTime() != null ? transaction.getDateTime().toLocalDate() : null;
    }

    @Override
    public Account getEntity() {
        return account;
    }

    @Override
    public Long getAmount() {
        return transaction.getAmount();
    }
}
