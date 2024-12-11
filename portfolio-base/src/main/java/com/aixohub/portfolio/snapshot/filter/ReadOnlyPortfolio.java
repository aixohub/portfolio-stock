package com.aixohub.portfolio.snapshot.filter;

import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;

import java.util.List;
import java.util.Objects;

public class ReadOnlyPortfolio extends Portfolio {
    private final Portfolio source;

    ReadOnlyPortfolio(Portfolio source) {
        this.source = Objects.requireNonNull(source);
        this.setName(source.getName());
    }

    public static Portfolio unwrap(Portfolio portfolio) {
        return portfolio instanceof ReadOnlyPortfolio readOnly ? unwrap(readOnly.source) : portfolio;
    }

    public Portfolio unwrap() {
        return source instanceof ReadOnlyPortfolio readOnly ? readOnly.unwrap() : source;
    }

    public Portfolio getSource() {
        return source;
    }

    @Override
    public void addTransaction(PortfolioTransaction transaction) {
        throw new UnsupportedOperationException();
    }

    void internalAddTransaction(PortfolioTransaction transaction) {
        super.addTransaction(transaction);
    }

    @Override
    public void shallowDeleteTransaction(PortfolioTransaction transaction, Client client) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAllTransaction(List<PortfolioTransaction> transactions) {
        throw new UnsupportedOperationException();
    }

    void internalAddAllTransaction(List<PortfolioTransaction> transactions) {
        super.addAllTransaction(transactions);
    }
}
