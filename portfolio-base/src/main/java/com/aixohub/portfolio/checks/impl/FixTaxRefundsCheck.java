package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;

import java.util.Collections;
import java.util.List;

/**
 * Due to bug #467, tax refunds can contain a reference to an empty security
 * when in fact the transaction should not be bound to a security at all. Remove
 * it.
 */
public class FixTaxRefundsCheck implements Check {

    @Override
    public List<Issue> execute(Client client) {
        // issue is fixed version 29
        if (client.getFileVersionAfterRead() > 29)
            return Collections.emptyList();

        for (Account account : client.getAccounts()) {
            for (AccountTransaction t : account.getTransactions()) {
                if (t.getType() != AccountTransaction.Type.TAX_REFUND)
                    continue;

                if (t.getSecurity() == null)
                    continue;

                Security security = t.getSecurity();

                if (!client.getSecurities().contains(security))
                    t.setSecurity(null);
            }
        }

        return Collections.emptyList();
    }

}
