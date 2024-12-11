package com.aixohub.portfolio.snapshot;

import com.aixohub.portfolio.model.Classification;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.money.MutableMoney;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class AssetCategory {
    private final Classification classification;
    private final CurrencyConverter converter;
    private final LocalDate date;
    private final List<AssetPosition> positions = new ArrayList<>();
    private final Money totalAssets;
    private final MutableMoney valuation;

    /* package */ AssetCategory(Classification classification, CurrencyConverter converter, LocalDate date,
                                Money totalAssets) {
        this.classification = classification;
        this.converter = converter;
        this.date = date;
        this.totalAssets = totalAssets;
        this.valuation = MutableMoney.of(converter.getTermCurrency());
    }

    public Money getValuation() {
        return this.valuation.toMoney();
    }

    public double getShare() {
        return (double) this.valuation.getAmount() / (double) this.totalAssets.getAmount();
    }

    public Classification getClassification() {
        return this.classification;
    }

    public List<AssetPosition> getPositions() {
        return positions;
    }

    public void addPosition(AssetPosition p) {
        this.positions.add(p);
        this.valuation.add(converter.convert(date, p.getValuation()));
    }
}
