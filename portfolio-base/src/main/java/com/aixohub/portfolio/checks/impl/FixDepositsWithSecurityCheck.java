package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Client;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Historically it was possible to assign a security to a deposit. This check
 * removes the illegal assignment.
 */
public class FixDepositsWithSecurityCheck implements Check {

    @Override
    public List<Issue> execute(Client client) {
        Set<AccountTransaction.Type> set = EnumSet.of(AccountTransaction.Type.DEPOSIT, //
                AccountTransaction.Type.REMOVAL, //
                AccountTransaction.Type.TRANSFER_IN, //
                AccountTransaction.Type.TRANSFER_OUT, //
                AccountTransaction.Type.INTEREST_CHARGE);

        client.getAccounts().stream() //
                .flatMap(a -> a.getTransactions().stream()) //
                .filter(t -> t.getSecurity() != null) //
                .filter(t -> set.contains(t.getType())) //
                .forEach(t -> t.setSecurity(null));

        return Collections.emptyList();
    }
}
