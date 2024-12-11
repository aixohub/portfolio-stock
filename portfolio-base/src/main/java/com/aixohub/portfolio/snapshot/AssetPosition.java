package com.aixohub.portfolio.snapshot;

import com.aixohub.portfolio.model.InvestmentVehicle;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.util.TextUtil;

import java.time.LocalDate;
import java.util.Comparator;

public class AssetPosition {
    private final SecurityPosition position;
    private final CurrencyConverter converter;
    private final LocalDate date;
    private final Money totalAssets;
    private final Money valuation;
    /* package */ AssetPosition(SecurityPosition position, CurrencyConverter converter, LocalDate date,
                                Money totalAssets) {
        this.position = position;
        this.converter = converter;
        this.date = date;
        this.totalAssets = totalAssets;
        this.valuation = position.calculateValue();
    }

    public Money getValuation() {
        return converter.convert(date, valuation);
    }

    public double getShare() {
        return (double) getValuation().getAmount() / (double) this.totalAssets.getAmount();
    }

    public String getDescription() {
        return position.getInvestmentVehicle().getName();
    }

    public Security getSecurity() {
        return position.getSecurity();
    }

    public SecurityPosition getPosition() {
        return position;
    }

    public InvestmentVehicle getInvestmentVehicle() {
        return position.getInvestmentVehicle();
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "AssetPosition [" + position.getInvestmentVehicle() + ", date=" + date + ", valuation=" + valuation
                + "]";
    }

    public static final class ByDescription implements Comparator<AssetPosition> {
        @Override
        public int compare(AssetPosition p1, AssetPosition p2) {
            return TextUtil.compare(p1.getDescription(), p2.getDescription());
        }
    }

}
