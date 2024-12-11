package com.aixohub.portfolio.snapshot.trail;

import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.Transaction;
import com.aixohub.portfolio.money.Money;

import java.time.LocalDate;

/* package */ class TransactionTrail implements TrailRecord {
    private final Transaction transaction;

    public TransactionTrail(Transaction t) {
        this.transaction = t;
    }

    @Override
    public LocalDate getDate() {
        return transaction.getDateTime().toLocalDate();
    }

    @Override
    public String getLabel() {
        if (transaction instanceof PortfolioTransaction pt)
            return pt.getType().toString();
        else if (transaction instanceof AccountTransaction at)
            return at.getType().toString();
        else
            return transaction.toString();
    }

    @Override
    public Long getShares() {
        return transaction.getShares();
    }

    @Override
    public Money getValue() {
        return transaction.getMonetaryAmount();
    }
}
