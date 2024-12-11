package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adds securities which are only present in transactions to the list of
 * securities. Before #602 it could happen that the user imports only the
 * transaction from PDF but explicitly does not import the newly created
 * security. That created orphaned securities.
 */
public class FixOrphanedSecurtiesCheck implements Check {

    @Override
    public List<Issue> execute(Client client) {
        Set<Security> missing = client.getPortfolios().stream() //
                .flatMap(p -> p.getTransactions().stream()) //
                .map(t -> t.getSecurity()) //
                .filter(s -> !client.getSecurities().contains(s)) //
                .collect(Collectors.toSet());

        missing.stream().forEach(security -> {
            security.setName(security.getName() + " " + Messages.LabelSuffixEntryCorrected); //$NON-NLS-1$
            client.addSecurity(security);
        });

        return Collections.emptyList();
    }

}
