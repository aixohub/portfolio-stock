package com.aixohub.portfolio.snapshot.security;

import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.util.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BaseSecurityPerformanceRecord {
    protected final Client client;
    protected final Security security;
    protected final CurrencyConverter converter;
    protected final Interval interval;
    protected final List<CalculationLineItem> lineItems = new ArrayList<>();
    public BaseSecurityPerformanceRecord(Client client, Security security, CurrencyConverter converter,
                                         Interval interval) {
        this.client = client;
        this.security = security;

        this.converter = converter;
        this.interval = interval;
    }

    public Security getSecurity() {
        return security;
    }

    public String getSecurityName() {
        return getSecurity().getName();
    }

    /* package */void addLineItem(CalculationLineItem item) {
        this.lineItems.add(item);
    }

    public List<CalculationLineItem> getLineItems() {
        return lineItems;
    }

    public enum Periodicity {
        UNKNOWN, NONE, INDEFINITE, ANNUAL, SEMIANNUAL, QUARTERLY, MONTHLY, IRREGULAR;

        private static final ResourceBundle RESOURCES = ResourceBundle
                .getBundle("com.aixohub.portfolio.snapshot.labels"); //$NON-NLS-1$

        @Override
        public String toString() {
            return RESOURCES.getString("dividends." + name()); //$NON-NLS-1$
        }
    }

    public interface Trails // NOSONAR
    {
        String FIFO_COST = "fifoCost"; //$NON-NLS-1$
        String REALIZED_CAPITAL_GAINS = "realizedCapitalGains"; //$NON-NLS-1$
        String REALIZED_CAPITAL_GAINS_FOREX = "realizedCapitalGainsForex"; //$NON-NLS-1$
        String UNREALIZED_CAPITAL_GAINS = "unrealizedCapitalGains"; //$NON-NLS-1$
        String UNREALIZED_CAPITAL_GAINS_FOREX = "unrealizedCapitalGainsForex"; //$NON-NLS-1$
    }

}
