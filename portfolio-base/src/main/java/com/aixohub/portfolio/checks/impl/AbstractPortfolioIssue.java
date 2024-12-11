package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;

import java.time.LocalDate;

/* package */abstract class AbstractPortfolioIssue implements Issue {
    protected Client client;
    protected Portfolio portfolio;
    protected PortfolioTransaction transaction;

    public AbstractPortfolioIssue(Client client, Portfolio portfolio, PortfolioTransaction transaction) {
        this.client = client;
        this.portfolio = portfolio;
        this.transaction = transaction;
    }

    @Override
    public LocalDate getDate() {
        return transaction != null ? transaction.getDateTime().toLocalDate() : null;
    }

    @Override
    public Portfolio getEntity() {
        return portfolio;
    }

    @Override
    public Long getAmount() {
        return transaction != null ? transaction.getAmount() : null;
    }
}
