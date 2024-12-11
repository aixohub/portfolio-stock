package com.aixohub.portfolio.math;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.SecurityPrice;
import com.aixohub.portfolio.util.Interval;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class AllTimeHigh {
    private final Security security;
    private final Interval interval;
    private Long athValue;
    private LocalDate athDate;
    private Double athDistanceInPercent;

    public AllTimeHigh(Security security, Interval interval) {
        this.security = security;
        this.interval = Objects.requireNonNull(interval);

        this.calculate();
    }

    private void calculate() {
        if (security == null || interval == null)
            return;

        Optional<SecurityPrice> max = security.getPricesIncludingLatest().stream() //
                .filter(p -> interval.contains(p.getDate())) //
                .max(Comparator.comparing(SecurityPrice::getValue));

        if (!max.isPresent())
            return;

        SecurityPrice latest = security.getSecurityPrice(interval.getEnd());

        this.athValue = max.get().getValue();
        this.athDate = max.get().getDate();
        this.athDistanceInPercent = (latest.getValue() - max.get().getValue()) / (double) max.get().getValue();
    }

    public Long getValue() {
        return this.athValue;
    }

    public Double getDistance() {
        return this.athDistanceInPercent;
    }

    public LocalDate getDate() {
        return this.athDate;
    }
}
