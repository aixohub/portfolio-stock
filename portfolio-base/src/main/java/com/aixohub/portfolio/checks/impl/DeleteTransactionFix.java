package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Transaction;
import com.aixohub.portfolio.model.TransactionOwner;
import com.aixohub.portfolio.model.TransactionPair;

/* package */class DeleteTransactionFix<T extends Transaction> implements QuickFix {
    private final Client client;
    private final TransactionOwner<T> owner;
    private final T transaction;

    public DeleteTransactionFix(Client client, TransactionOwner<T> owner, T transaction) {
        this.client = client;
        this.owner = owner;
        this.transaction = transaction;
    }

    public DeleteTransactionFix(Client client, TransactionPair<T> tx) {
        this(client, tx.getOwner(), tx.getTransaction());
    }

    @Override
    public String getLabel() {
        return Messages.FixDeleteTransaction;
    }

    @Override
    public String getDoneLabel() {
        return Messages.FixDeleteTransactionDone;
    }

    @Override
    public void execute() {
        owner.deleteTransaction(transaction, client);
    }
}
