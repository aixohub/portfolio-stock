package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.SecurityPrice;

import java.util.Collections;
import java.util.List;

/**
 * Removes any null values from the security price list. This check fixes a
 * NullPointerException reported in the forum although it is unclear how null
 * values have been added to the security price list in the first place.
 */
public class NullSecurityPricesCheck implements Check {

    @Override
    public List<Issue> execute(Client client) {
        for (Security security : client.getSecurities()) {
            for (SecurityPrice price : security.getPrices()) {
                if (price == null) {
                    security.removePrice(null);

                    // multiple null values cannot exist due to the binary
                    // search / replacement logic that fails when adding the
                    // second null value

                    break;
                }
            }
        }

        return Collections.emptyList();
    }
}
