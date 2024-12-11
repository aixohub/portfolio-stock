package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.model.BuySellEntry;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.Transaction.Unit;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search for transactions of type TRANSFER_IN where the crossEntry is an
 * instance of BuySellEntry instead of PortfolioTransferEntry. This indicates
 * that this malformed transaction was created through PDF import and needs to
 * be corrected. Issue #1931 relates to this fix.
 */
public class FixWrongTransfersCheck implements Check {

    @Override
    public List<Issue> execute(Client client) {

        List<PortfolioTransaction> transactions = client.getPortfolios().stream()
                .flatMap(p -> p.getTransactions().stream())
                .filter(t -> PortfolioTransaction.Type.TRANSFER_IN.equals(t.getType()))
                .collect(Collectors.toList());

        for (PortfolioTransaction t : transactions) {
            if (t.getCrossEntry() instanceof BuySellEntry) {
                // create new transaction because crossEntry cannot be removed
                PortfolioTransaction copy = new PortfolioTransaction(t.getDateTime(), t.getCurrencyCode(),
                        t.getAmount(), t.getSecurity(), t.getShares(),
                        PortfolioTransaction.Type.DELIVERY_INBOUND, t.getUnitSum(Unit.Type.FEE).getAmount(),
                        t.getUnitSum(Unit.Type.TAX).getAmount());
                // copy note text
                copy.setNote(t.getNote());

                // replace transaction in portfolio
                ((Portfolio) t.getCrossEntry().getOwner(t)).addTransaction(copy);
                ((Portfolio) t.getCrossEntry().getOwner(t)).deleteTransaction(t, client);
            }
        }

        return Collections.emptyList();
    }
}
