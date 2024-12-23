package com.aixohub.portfolio.money.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.PortfolioLog;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.SecurityPrice;
import com.aixohub.portfolio.money.ExchangeRate;
import com.aixohub.portfolio.money.ExchangeRateProvider;
import com.aixohub.portfolio.money.ExchangeRateTimeSeries;
import com.aixohub.portfolio.money.Values;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An exchange rate provider based on securities tagged with two currencies.
 */
public class SecurityBasedExchangeRateProvider implements ExchangeRateProvider {
    @Override
    public List<ExchangeRateTimeSeries> getAvailableTimeSeries(Client client) {
        // collect all securities that are exchange rates
        List<ExchangeRateTimeSeries> answer = new ArrayList<>();
        if (client != null) {
            for (Security security : client.getSecurities()) {
                if (security.isExchangeRate()) {
                    answer.add(new SecurityBasedExchangeRate(security));
                }
            }
        }
        return answer;
    }

    @Override
    public String getName() {
        return Messages.SecurityBasedExchangeRateProvider;
    }

    private class SecurityBasedExchangeRate implements ExchangeRateTimeSeries {
        private final Security security;
        private boolean hasWarningLogged = false;

        public SecurityBasedExchangeRate(Security security) {
            this.security = security;
        }

        @Override
        public String getBaseCurrency() {
            return security.getCurrencyCode();
        }

        @Override
        public Optional<ExchangeRateProvider> getProvider() {
            return Optional.of(SecurityBasedExchangeRateProvider.this);
        }

        @Override
        public List<ExchangeRate> getRates() {
            List<ExchangeRate> answer = new ArrayList<>();
            // turn all security prices into exchange rate
            for (SecurityPrice price : security.getPricesIncludingLatest()) {
                answer.add(toExchangeRate(price));
            }
            return answer;
        }

        @Override
        public String getTermCurrency() {
            return security.getTargetCurrencyCode();
        }

        @Override
        public int getWeight() {
            return 1;
        }

        @Override
        public Optional<ExchangeRate> lookupRate(LocalDate requestedTime) {
            SecurityPrice price = security.getSecurityPrice(requestedTime);

            // if neither latest nor historic prices exist, then
            // #getSecurityPrice returns a price with the value of zero
            if (price != null && price.getValue() != 0)
                return Optional.of(toExchangeRate(price));

            // log warning about missing exchange rates. This message will be
            // printed only if there are no rates at all. To avoid overflow the
            // log, this message is logged only once.
            if (!hasWarningLogged) {
                PortfolioLog.warning(MessageFormat.format(Messages.MsgNoExchangeRatesAvailableForCustomSeries,
                        security.getName(), security.getCurrencyCode(), security.getTargetCurrencyCode()));
                hasWarningLogged = true;
            }

            return Optional.empty();
        }

        /**
         * Converts a price of a security to an {@link ExchangeRate}.
         *
         * @param price {@link SecurityPrice}
         * @return {@link ExchangeRate}
         */
        private ExchangeRate toExchangeRate(SecurityPrice price) {
            return new ExchangeRate(price.getDate(), BigDecimal.valueOf(price.getValue() / Values.Quote.divider()));
        }

        @Override
        public String getLabel() {
            return MessageFormat.format(Messages.LabelExchangeRateSeriesBasedOnSecurity,
                    ExchangeRateTimeSeries.super.getLabel(), security.getName());
        }

    }
}
