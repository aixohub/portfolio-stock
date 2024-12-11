package com.aixohub.portfolio.snapshot.filter;

import com.aixohub.portfolio.model.Client;

/**
 * Filters accounts, portfolios, or transactions in order to calculate
 * performance indicators on a sub set of the whole client. Use
 * {@link ReadOnlyClient}, {@link ReadOnlyPortfolio}, and
 * {@link ReadOnlyAccount} to make sure that any model objects accidentally
 * leaked to the UI cannot be used.
 */
@FunctionalInterface
public interface ClientFilter {
    ClientFilter NO_FILTER = client -> client;

    Client filter(Client client);
}
