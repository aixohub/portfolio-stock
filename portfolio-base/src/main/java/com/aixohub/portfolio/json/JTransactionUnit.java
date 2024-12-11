package com.aixohub.portfolio.json;

import com.aixohub.portfolio.model.Transaction.Unit;
import com.aixohub.portfolio.money.Values;

import java.math.BigDecimal;

public class JTransactionUnit {
    private Unit.Type type;
    private Double amount;
    private String fxCurrency;
    private Double fxAmount;
    private BigDecimal fxRateToBase;

    public static JTransactionUnit from(Unit unit) {
        JTransactionUnit u = new JTransactionUnit();
        u.type = unit.getType();
        u.amount = unit.getAmount().getAmount() / Values.Amount.divider();

        if (unit.getForex() != null) {
            u.fxCurrency = unit.getForex().getCurrencyCode();
            u.fxAmount = unit.getForex().getAmount() / Values.Amount.divider();
            u.fxRateToBase = unit.getExchangeRate();
        }

        return u;
    }

    public Unit.Type getType() {
        return type;
    }

    public void setType(Unit.Type type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getFxCurrency() {
        return fxCurrency;
    }

    public void setFxCurrency(String fxCurrency) {
        this.fxCurrency = fxCurrency;
    }

    public Double getFxAmount() {
        return fxAmount;
    }

    public void setFxAmount(Double fxAmount) {
        this.fxAmount = fxAmount;
    }

    public BigDecimal getFxRateToBase() {
        return fxRateToBase;
    }

    public void setFxRateToBase(BigDecimal fxRateToBase) {
        this.fxRateToBase = fxRateToBase;
    }
}
