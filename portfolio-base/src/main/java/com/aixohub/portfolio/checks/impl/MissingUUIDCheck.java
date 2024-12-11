package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.model.Client;

import java.util.Collections;
import java.util.List;

/**
 * As per issue #571 some securities are missing UUIDs.
 */
public class MissingUUIDCheck implements Check {

    @Override
    public List<Issue> execute(Client client) {
        client.getSecurities().stream().filter(s -> s.getUUID() == null).forEach(s -> s.fixMissingUUID());
        return Collections.emptyList();
    }

}
