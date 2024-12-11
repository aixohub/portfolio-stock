package com.aixohub.portfolio.snapshot.trades;

import com.aixohub.portfolio.math.IRR;
import com.aixohub.portfolio.model.Adaptable;
import com.aixohub.portfolio.model.Named;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.TransactionPair;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.money.MoneyCollectors;
import com.aixohub.portfolio.money.Values;
import com.aixohub.portfolio.util.Dates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Trade implements Adaptable {
    private final Security security;
    private final Portfolio portfolio;
    private final long shares;
    private LocalDateTime start;
    private LocalDateTime end;
    private final List<TransactionPair<PortfolioTransaction>> transactions = new ArrayList<>();

    private Money entryValue;
    private Money entryGrossValue;
    private Money exitValue;
    private Money exitGrossValue;
    private long holdingPeriod;
    private double irr;

    public Trade(Security security, Portfolio portfolio, long shares) {
        this.security = security;
        this.shares = shares;
        this.portfolio = portfolio;
    }

    /* package */ void calculate(CurrencyConverter converter) {
        this.entryValue = transactions.stream() //
                .filter(t -> t.getTransaction().getType().isPurchase())
                .map(t -> t.getTransaction().getMonetaryAmount()
                        .with(converter.at(t.getTransaction().getDateTime())))
                .collect(MoneyCollectors.sum(converter.getTermCurrency()));

        this.entryGrossValue = transactions.stream() //
                .filter(t -> t.getTransaction().getType().isPurchase())
                .map(t -> t.getTransaction().getGrossValue()
                        .with(converter.at(t.getTransaction().getDateTime())))
                .collect(MoneyCollectors.sum(converter.getTermCurrency()));

        if (end != null) {
            this.exitValue = transactions.stream() //
                    .filter(t -> t.getTransaction().getType().isLiquidation())
                    .map(t -> t.getTransaction().getMonetaryAmount()
                            .with(converter.at(t.getTransaction().getDateTime())))
                    .collect(MoneyCollectors.sum(converter.getTermCurrency()));

            this.exitGrossValue = transactions.stream() //
                    .filter(t -> t.getTransaction().getType().isLiquidation())
                    .map(t -> t.getTransaction().getGrossValue()
                            .with(converter.at(t.getTransaction().getDateTime())))
                    .collect(MoneyCollectors.sum(converter.getTermCurrency()));

            this.holdingPeriod = Math.round(transactions.stream() //
                    .filter(t -> t.getTransaction().getType().isPurchase())
                    .mapToLong(t -> t.getTransaction().getShares() * Dates.daysBetween(
                            t.getTransaction().getDateTime().toLocalDate(), end.toLocalDate()))
                    .sum() / (double) shares);
        } else {
            LocalDate now = LocalDate.now();

            long marketValue = BigDecimal.valueOf(shares) //
                    .movePointLeft(Values.Share.precision()) //
                    .multiply(BigDecimal.valueOf(security.getSecurityPrice(now).getValue()), Values.MC)
                    .movePointLeft(Values.Quote.precisionDeltaToMoney()) //
                    .setScale(0, RoundingMode.HALF_UP).longValue();

            this.exitValue = converter.at(now).apply(Money.of(security.getCurrencyCode(), marketValue));

            this.holdingPeriod = Math.round(transactions.stream() //
                    .filter(t -> t.getTransaction().getType().isPurchase())
                    .mapToLong(t -> t.getTransaction().getShares()
                            * Dates.daysBetween(t.getTransaction().getDateTime().toLocalDate(), now))
                    .sum() / (double) shares);
        }

        // let's sort again because the list might not be sorted anymore due to
        // transfers
        Collections.sort(transactions, TransactionPair.BY_DATE);

        // re-set start date from first entry after sorting
        this.setStart(transactions.get(0).getTransaction().getDateTime());

        calculateIRR(converter);
    }

    private void calculateIRR(CurrencyConverter converter) {
        List<LocalDate> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        transactions.stream().forEach(t -> {
            dates.add(t.getTransaction().getDateTime().toLocalDate());

            double amount = t.getTransaction().getMonetaryAmount().with(converter.at(t.getTransaction().getDateTime()))
                    .getAmount() / Values.Amount.divider();

            if (t.getTransaction().getType().isPurchase())
                amount = -amount;

            values.add(amount);
        });

        if (end == null) {
            dates.add(LocalDate.now());
            values.add(exitValue.getAmount() / Values.Amount.divider());
        }

        this.irr = IRR.calculate(dates, values);
    }

    public Security getSecurity() {
        return security;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public Optional<LocalDateTime> getEnd() {
        return Optional.ofNullable(end);
    }

    /* package */ void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    /* package */ void setStart(LocalDateTime start) {
        this.start = start;
    }

    public long getShares() {
        return shares;
    }

    public List<TransactionPair<PortfolioTransaction>> getTransactions() {
        return transactions;
    }

    public TransactionPair<PortfolioTransaction> getLastTransaction() {
        // transactions have been sorted by calculate(), which is called once
        // after creation
        return transactions.get(transactions.size() - 1);
    }

    /**
     * Returns the transaction that closed the trade (if the trade is closed)
     */
    public Optional<TransactionPair<PortfolioTransaction>> getClosingTransaction() {
        return isClosed() ? Optional.of(transactions.get(transactions.size() - 1)) : Optional.empty();
    }

    public Money getEntryValue() {
        return entryValue;
    }

    public Money getExitValue() {
        return exitValue;
    }

    public Money getProfitLoss() {
        return exitValue.subtract(entryValue);
    }

    public Money getGrossProfitLoss() {
        if (exitGrossValue == null)
            return null;
        return exitGrossValue.subtract(entryGrossValue);
    }

    public long getHoldingPeriod() {
        return holdingPeriod;
    }

    public double getIRR() {
        return irr;
    }

    public double getReturn() {
        return (exitValue.getAmount() / (double) entryValue.getAmount()) - 1;
    }

    /**
     * @return True if the trade has been closed, false otherwise
     * @brief Checks if the trade is closed
     */
    public boolean isClosed() {
        return this.getEnd().isPresent();
    }

    /**
     * @return True if the trade resulted in a net loss
     * @brief Checks if the trade made a net loss
     */
    public boolean isLoss() {
        return this.getProfitLoss().isNegative();
    }

    /**
     * @return True if the trade result in a gross loss
     * @brief Check if the trade made a gross gross
     */
    public boolean isGrossLoss() {
        return this.getGrossProfitLoss().isNegative();
    }

    @Override
    public <T> T adapt(Class<T> type) {
        if (type == Security.class || type == Named.class)
            return type.cast(security);
        else
            return null;
    }
}
