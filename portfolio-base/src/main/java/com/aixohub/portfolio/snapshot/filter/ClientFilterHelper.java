package com.aixohub.portfolio.snapshot.filter;

import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransferEntry;
import com.aixohub.portfolio.model.Classification;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.PortfolioTransferEntry;
import com.aixohub.portfolio.money.Values;

import java.math.BigDecimal;
import java.math.RoundingMode;

/* protected */ class ClientFilterHelper {
    private ClientFilterHelper() {
    }

    /* package */
    static void recreateTransfer(PortfolioTransferEntry transferEntry, ReadOnlyPortfolio sourcePortfolio,
                                 ReadOnlyPortfolio targetPortfolio) {
        recreateTransfer(transferEntry, sourcePortfolio, targetPortfolio, Classification.ONE_HUNDRED_PERCENT_BD);
    }

    /* package */
    static void recreateTransfer(PortfolioTransferEntry transferEntry, ReadOnlyPortfolio sourcePortfolio,
                                 ReadOnlyPortfolio targetPortfolio, BigDecimal weight) {
        PortfolioTransaction t = transferEntry.getSourceTransaction();

        PortfolioTransferEntry copy = new PortfolioTransferEntry(sourcePortfolio, targetPortfolio);
        copy.setDate(t.getDateTime());
        copy.setCurrencyCode(t.getCurrencyCode());
        copy.setSecurity(t.getSecurity());
        copy.setNote(t.getNote());
        copy.setShares(value(t.getShares(), weight));
        copy.setAmount(value(t.getAmount(), weight));

        sourcePortfolio.internalAddTransaction(copy.getSourceTransaction());
        targetPortfolio.internalAddTransaction(copy.getTargetTransaction());
    }

    /* package */
    static void recreateTransfer(AccountTransferEntry transferEntry, ReadOnlyAccount sourceAccount,
                                 ReadOnlyAccount targetAccount) {
        AccountTransaction t = transferEntry.getSourceTransaction();

        AccountTransferEntry copy = new AccountTransferEntry(sourceAccount, targetAccount);

        copy.setDate(t.getDateTime());
        copy.setNote(t.getNote());

        copy.getSourceTransaction().setCurrencyCode(t.getCurrencyCode());
        copy.getSourceTransaction().setAmount(t.getAmount());
        copy.getSourceTransaction().addUnits(t.getUnits());

        AccountTransaction tt = transferEntry.getTargetTransaction();
        copy.getTargetTransaction().setCurrencyCode(tt.getCurrencyCode());
        copy.getTargetTransaction().setAmount(tt.getAmount());

        sourceAccount.internalAddTransaction(copy.getSourceTransaction());
        targetAccount.internalAddTransaction(copy.getTargetTransaction());
    }

    private static long value(long value, BigDecimal weight) {
        if (weight.equals(Classification.ONE_HUNDRED_PERCENT_BD))
            return value;
        else
            return BigDecimal.valueOf(value) //
                    .multiply(weight, Values.MC)
                    .divide(Classification.ONE_HUNDRED_PERCENT_BD, Values.MC)
                    .setScale(0, RoundingMode.HALF_EVEN).longValue();
    }
}
