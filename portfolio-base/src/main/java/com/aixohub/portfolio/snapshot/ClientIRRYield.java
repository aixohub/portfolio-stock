package com.aixohub.portfolio.snapshot;

import com.aixohub.portfolio.math.IRR;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransaction.Type;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.Transaction;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.money.Values;
import com.aixohub.portfolio.util.Interval;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientIRRYield {
    private final double irr;

    private ClientIRRYield(double irr) {
        this.irr = irr;
    }

    public static ClientIRRYield create(Client client, ClientSnapshot snapshotStart, ClientSnapshot snapshotEnd) {
        Interval interval = Interval.of(snapshotStart.getTime(), snapshotEnd.getTime());

        List<Transaction> transactions = new ArrayList<>();
        collectAccountTransactions(client, interval, transactions);
        collectPortfolioTransactions(client, interval, transactions);
        Collections.sort(transactions, Transaction.BY_DATE);

        List<LocalDate> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        collectDatesAndValues(interval, snapshotStart, snapshotEnd, transactions, dates, values);

        double irr = 0d;

        // calculate IRR only if there any transaction or valuations at all.
        // There might be none in the selected interval.
        if (!values.isEmpty())
            irr = IRR.calculate(dates, values);

        return new ClientIRRYield(irr);
    }

    private static void collectPortfolioTransactions(Client client, Interval interval, List<Transaction> transactions) {
        for (Portfolio portfolio : client.getPortfolios()) {
            portfolio.getTransactions().stream() //
                    .filter(t -> interval.contains(t.getDateTime())) //
                    .forEach(t -> {
                        switch (t.getType()) {
                            case TRANSFER_IN:
                            case TRANSFER_OUT:
                            case DELIVERY_INBOUND:
                            case DELIVERY_OUTBOUND:
                                transactions.add(t);
                                break;
                            case BUY:
                            case SELL:
                                break;
                            default:
                                throw new UnsupportedOperationException();
                        }
                    });
        }
    }

    private static void collectAccountTransactions(Client client, Interval interval, List<Transaction> transactions) {
        for (Account account : client.getAccounts()) {
            account.getTransactions().stream() //
                    .filter(t -> interval.contains(t.getDateTime())) //
                    .forEach(t -> {
                        switch (t.getType()) {
                            case DEPOSIT:
                            case REMOVAL:
                            case TRANSFER_IN:
                            case TRANSFER_OUT:
                                transactions.add(t);
                                break;
                            case BUY:
                            case SELL:
                            case FEES:
                            case FEES_REFUND:
                            case TAXES:
                            case DIVIDENDS:
                            case INTEREST:
                            case INTEREST_CHARGE:
                            case TAX_REFUND:
                                break;
                            default:
                                throw new UnsupportedOperationException();
                        }
                    });
        }
    }

    private static void collectDatesAndValues(Interval interval, ClientSnapshot snapshotStart,
                                              ClientSnapshot snapshotEnd, List<Transaction> transactions, List<LocalDate> dates,
                                              List<Double> values) {
        CurrencyConverter converter = snapshotStart.getCurrencyConverter();

        // add start day only if there is already a valuation (the interval
        // period might start way before the first transaction)

        if (!snapshotStart.getMonetaryAssets().isZero()) {
            dates.add(interval.getStart());
            // snapshots are always in target currency, no conversion needed
            values.add(-snapshotStart.getMonetaryAssets().getAmount() / Values.Amount.divider());
        }

        for (Transaction t : transactions) {
            dates.add(t.getDateTime().toLocalDate());

            if (t instanceof AccountTransaction at) {
                long amount = converter.convert(t.getDateTime(), t.getMonetaryAmount()).getAmount();
                if (at.getType() == Type.DEPOSIT || at.getType() == Type.TRANSFER_IN)
                    amount = -amount;
                values.add(amount / Values.Amount.divider());
            } else if (t instanceof PortfolioTransaction pt) {
                long amount = converter.convert(t.getDateTime(), t.getMonetaryAmount()).getAmount();
                if (pt.getType() == PortfolioTransaction.Type.DELIVERY_INBOUND
                        || pt.getType() == PortfolioTransaction.Type.TRANSFER_IN)
                    amount = -amount;
                values.add(amount / Values.Amount.divider());
            } else {
                throw new UnsupportedOperationException();
            }
        }

        if (!snapshotEnd.getMonetaryAssets().isZero()) {
            dates.add(interval.getEnd());
            values.add(snapshotEnd.getMonetaryAssets().getAmount() / Values.Amount.divider());
        }
    }

    public double getIrr() {
        return irr;
    }
}
