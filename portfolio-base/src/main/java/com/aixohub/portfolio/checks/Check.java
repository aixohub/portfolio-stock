package com.aixohub.portfolio.checks;

import com.aixohub.portfolio.model.Client;

import java.util.List;

public interface Check {
    /**
     * Execute a consistency check on the given client.
     *
     * @return list of issues; empty list if no issues are found
     */
    List<Issue> execute(Client client);
}
