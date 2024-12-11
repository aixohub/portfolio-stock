package com.aixohub.portfolio.money.impl;

import com.aixohub.portfolio.money.ExchangeRate;
import com.aixohub.portfolio.money.ExchangeRateProvider;
import com.aixohub.portfolio.money.ExchangeRateTimeSeries;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EmptyExchangeRateTimeSeries implements ExchangeRateTimeSeries {
    private final ExchangeRate exchangeRate = new ExchangeRate(LocalDate.now(), BigDecimal.ONE);
    private final String baseCurrency;
    private final String termCurrency;

    public EmptyExchangeRateTimeSeries(String baseCurrency, String termCurrency) {
        this.baseCurrency = baseCurrency;
        this.termCurrency = termCurrency;
    }

    @Override
    public String getBaseCurrency() {
        return baseCurrency;
    }

    @Override
    public String getTermCurrency() {
        return termCurrency;
    }

    @Override
    public Optional<ExchangeRateProvider> getProvider() {
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> getRates() {
        return List.of(exchangeRate);
    }

    @Override
    public Optional<ExchangeRate> lookupRate(LocalDate requestedTime) {
        return Optional.of(exchangeRate);
    }

    @Override
    public int getWeight() {
        return 1;
    }
}
