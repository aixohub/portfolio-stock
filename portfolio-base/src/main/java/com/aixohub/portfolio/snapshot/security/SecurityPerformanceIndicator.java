package com.aixohub.portfolio.snapshot.security;

import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.money.Quote;
import com.aixohub.portfolio.snapshot.trail.TrailRecord;

public interface SecurityPerformanceIndicator {
    interface Costs extends SecurityPerformanceIndicator {
        Money getFifoCost();

        Money getMovingAverageCost();

        Quote getFifoCostPerSharesHeld();

        Quote getMovingAverageCostPerSharesHeld();
    }

    interface CapitalGains extends SecurityPerformanceIndicator {
        Money getCapitalGains();

        TrailRecord getCapitalGainsTrail();

        Money getForexCaptialGains();

        TrailRecord getForexCapitalGainsTrail();
    }

}
