package com.aixohub.portfolio.snapshot.security;

import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.util.Interval;

import java.util.List;
import java.util.Optional;

public class LazySecurityPerformanceSnapshot {
    private final List<LazySecurityPerformanceRecord> records;

    private LazySecurityPerformanceSnapshot(List<LazySecurityPerformanceRecord> records) {
        this.records = records;
    }

    public static LazySecurityPerformanceSnapshot create(Client client, CurrencyConverter converter, Interval interval) {
        var records = new SecurityPerformanceSnapshotBuilder<LazySecurityPerformanceRecord>(client, converter, interval)
                .create(LazySecurityPerformanceRecord.class);

        return new LazySecurityPerformanceSnapshot(records);
    }

    public List<LazySecurityPerformanceRecord> getRecords() {
        return records;
    }

    public Optional<LazySecurityPerformanceRecord> getRecord(Security security) {
        return records.stream().filter(r -> security.equals(r.getSecurity())).findAny();
    }
}
