package com.aixohub.portfolio.snapshot.security;

import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.snapshot.ClientSnapshot;
import com.aixohub.portfolio.snapshot.filter.PortfolioClientFilter;
import com.aixohub.portfolio.util.Interval;

import java.util.List;
import java.util.Optional;

public class SecurityPerformanceSnapshot {
    private final List<SecurityPerformanceRecord> records;

    private SecurityPerformanceSnapshot(List<SecurityPerformanceRecord> records) {
        this.records = records;
    }

    @SafeVarargs
    public static SecurityPerformanceSnapshot create(Client client, CurrencyConverter converter, Interval interval,
                                                     Class<? extends SecurityPerformanceIndicator>... indicators) {
        var records = new SecurityPerformanceSnapshotBuilder<SecurityPerformanceRecord>(client, converter, interval)
                .create(SecurityPerformanceRecord.class);

        return doCreateSnapshot(records, indicators);
    }

    public static SecurityPerformanceSnapshot create(Client client, CurrencyConverter converter, Portfolio portfolio,
                                                     Interval interval) {
        return create(new PortfolioClientFilter(portfolio).filter(client), converter, interval);
    }

    @SafeVarargs
    public static SecurityPerformanceSnapshot create(Client client, CurrencyConverter converter, Interval interval,
                                                     ClientSnapshot valuationAtStart, ClientSnapshot valuationAtEnd,
                                                     Class<? extends SecurityPerformanceIndicator>... indicators) {
        var records = new SecurityPerformanceSnapshotBuilder<SecurityPerformanceRecord>(client, converter, interval)
                .create(SecurityPerformanceRecord.class, valuationAtStart, valuationAtEnd);
        return doCreateSnapshot(records, indicators);
    }

    @SafeVarargs
    private static SecurityPerformanceSnapshot doCreateSnapshot(List<SecurityPerformanceRecord> records,
                                                                Class<? extends SecurityPerformanceIndicator>... indicators) {
        records.forEach(r -> r.calculate(indicators));
        return new SecurityPerformanceSnapshot(records);
    }

    public List<SecurityPerformanceRecord> getRecords() {
        return records;
    }

    public Optional<SecurityPerformanceRecord> getRecord(Security security) {
        return records.stream().filter(r -> security.equals(r.getSecurity())).findAny();
    }
}
