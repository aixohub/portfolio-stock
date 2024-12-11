package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.money.Values;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharesHeldConsistencyCheck implements Check {

    @Override
    public List<Issue> execute(Client client) {
        List<Issue> issues = new ArrayList<Issue>();
        List<Security> securities = client.getSecurities();

        for (Portfolio portfolio : client.getPortfolios()) {
            long[] shares = new long[securities.size()];

            for (PortfolioTransaction t : portfolio.getTransactions()) {
                int index = securities.indexOf(t.getSecurity());

                // negative index means either the security is not known to the
                // global collection or the security is null -> other checks
                if (index < 0)
                    continue;

                switch (t.getType()) {
                    case BUY:
                    case TRANSFER_IN:
                    case DELIVERY_INBOUND:
                        shares[index] += t.getShares();
                        break;
                    case SELL:
                    case TRANSFER_OUT:
                    case DELIVERY_OUTBOUND:
                        shares[index] -= t.getShares();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
            }

            for (int ii = 0; ii < shares.length; ii++) {
                if (shares[ii] < 0) {
                    Security security = securities.get(ii);
                    issues.add(new SharesIssue(portfolio, security, shares[ii]));
                }
            }
        }

        return issues;
    }

    private static class SharesIssue implements Issue {
        private final Portfolio portfolio;
        private final Security security;
        private final long shares;

        public SharesIssue(Portfolio portfolio, Security security, long shares) {
            super();
            this.portfolio = portfolio;
            this.security = security;
            this.shares = shares;
        }

        @Override
        public LocalDate getDate() {
            return null;
        }

        @Override
        public Object getEntity() {
            return portfolio;
        }

        @Override
        public Long getAmount() {
            return null;
        }

        @Override
        public String getLabel() {
            return MessageFormat.format(Messages.IssueInconsistentSharesHeld, security, portfolio,
                    Values.Share.format(shares));
        }

        @Override
        public List<QuickFix> getAvailableFixes() {
            return Collections.emptyList();
        }
    }
}
