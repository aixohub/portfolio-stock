package com.aixohub.portfolio.snapshot.security;

import com.aixohub.portfolio.PortfolioLog;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.snapshot.ClientSnapshot;
import com.aixohub.portfolio.snapshot.PortfolioSnapshot;
import com.aixohub.portfolio.snapshot.SecurityPosition;
import com.aixohub.portfolio.util.Interval;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* package */ class SecurityPerformanceSnapshotBuilder<T extends BaseSecurityPerformanceRecord> {
    private final Client client;
    private final CurrencyConverter converter;
    private final Interval interval;

    public SecurityPerformanceSnapshotBuilder(Client client, CurrencyConverter converter, Interval interval) {
        this.client = client;
        this.converter = converter;
        this.interval = interval;
    }

    public List<T> create(Class<T> type) {
        Map<Security, T> transactions = initRecords(type);

        for (Account account : client.getAccounts()) {
            extractSecurityRelatedAccountTransactions(account, transactions);
        }

        for (Portfolio portfolio : client.getPortfolios()) {
            extractSecurityRelatedPortfolioTransactions(type, portfolio, transactions);
            addPseudoValuationTansactions(portfolio, transactions);
        }

        transactions.values()
                .forEach(item -> Collections.sort(item.getLineItems(), new CalculationLineItemComparator()));

        return transactions.values().stream().filter(item -> !item.getLineItems().isEmpty()).toList();
    }

    public List<T> create(Class<T> type, ClientSnapshot valuationAtStart, ClientSnapshot valuationAtEnd) {
        Map<Security, T> transactions = initRecords(type);

        for (Account account : client.getAccounts())
            extractSecurityRelatedAccountTransactions(account, transactions);

        for (Portfolio portfolio : client.getPortfolios())
            extractSecurityRelatedPortfolioTransactions(type, portfolio, transactions);

        for (PortfolioSnapshot snapshot : valuationAtStart.getPortfolios()) {
            for (SecurityPosition position : snapshot.getPositions()) {
                transactions.get(position.getSecurity()).addLineItem(CalculationLineItem
                        .atStart(snapshot.getPortfolio(), position, interval.getStart().atStartOfDay()));
            }
        }

        for (PortfolioSnapshot snapshot : valuationAtEnd.getPortfolios()) {
            for (SecurityPosition position : snapshot.getPositions()) {
                transactions.get(position.getSecurity()).addLineItem(CalculationLineItem.atEnd(snapshot.getPortfolio(),
                        position, interval.getEnd().atStartOfDay()));
            }
        }

        transactions.values()
                .forEach(item -> Collections.sort(item.getLineItems(), new CalculationLineItemComparator()));

        return transactions.values().stream().filter(item -> !item.getLineItems().isEmpty()).toList();
    }

    private Map<Security, T> initRecords(Class<T> type) {
        Map<Security, T> records = new HashMap<>();

        try {
            var typeConstructor = type.getDeclaredConstructor(Client.class, Security.class, CurrencyConverter.class,
                    Interval.class);

            for (Security s : client.getSecurities()) {
                records.put(s, typeConstructor.newInstance(client, s, converter, interval));
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }

        return records;
    }

    private void extractSecurityRelatedAccountTransactions(Account account, Map<Security, T> records) {
        for (AccountTransaction t : account.getTransactions()) // NOSONAR
        {
            if (t.getSecurity() == null)
                continue;

            if (!interval.contains(t.getDateTime()))
                continue;

            switch (t.getType()) {
                case DIVIDENDS, INTEREST, TAXES, TAX_REFUND, FEES, FEES_REFUND:
                    records.get(t.getSecurity()).addLineItem(CalculationLineItem.of(account, t));
                    break;
                case BUY, SELL:
                    break;
                case DEPOSIT, REMOVAL, TRANSFER_IN, TRANSFER_OUT, INTEREST_CHARGE:
                default:
                    throw new IllegalArgumentException(t.toString());
            }
        }
    }

    private void extractSecurityRelatedPortfolioTransactions(Class<T> type, Portfolio portfolio,
                                                             Map<Security, T> records) {
        portfolio.getTransactions().stream() //
                .filter(t -> interval.contains(t.getDateTime())) //
                .forEach(t -> records.computeIfAbsent(t.getSecurity(), s -> {

                    // must not happen because the records map is filled
                    // with _all_ securities of the client. However,
                    // #1836 reports a NPE exception here. Create a
                    // builder object to collect the transaction anyway.

                    PortfolioLog.warning(MessageFormat.format("Unidentified security ''{0}'' with UUID {1}", //$NON-NLS-1$
                            s.getName(), s.getUUID()));

                    try {
                        var typeConstructor = type.getDeclaredConstructor(Client.class, Security.class,
                                CurrencyConverter.class, Interval.class);
                        return typeConstructor.newInstance(client, s, converter, interval);
                    } catch (ReflectiveOperationException e) {
                        throw new IllegalArgumentException(e);
                    }
                }).addLineItem(CalculationLineItem.of(portfolio, t)));
    }

    private void addPseudoValuationTansactions(Portfolio portfolio, Map<Security, T> records) {
        PortfolioSnapshot snapshot = PortfolioSnapshot.create(portfolio, converter, interval.getStart());
        for (SecurityPosition position : snapshot.getPositions()) {
            records.get(position.getSecurity()).addLineItem(
                    CalculationLineItem.atStart(portfolio, position, interval.getStart().atStartOfDay()));
        }

        snapshot = PortfolioSnapshot.create(portfolio, converter, interval.getEnd());
        for (SecurityPosition position : snapshot.getPositions()) {
            records.get(position.getSecurity()).addLineItem(
                    CalculationLineItem.atEnd(portfolio, position, interval.getEnd().atStartOfDay()));
        }
    }
}
