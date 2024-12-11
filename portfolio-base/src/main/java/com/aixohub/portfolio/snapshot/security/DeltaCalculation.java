package com.aixohub.portfolio.snapshot.security;

import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransaction.Type;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.money.MutableMoney;

/* package */class DeltaCalculation extends Calculation {
    private MutableMoney delta;
    private MutableMoney cost;

    @Override
    public void setTermCurrency(String termCurrency) {
        super.setTermCurrency(termCurrency);
        this.delta = MutableMoney.of(termCurrency);
        this.cost = MutableMoney.of(termCurrency);
    }

    @Override
    public void visit(CurrencyConverter converter, CalculationLineItem.ValuationAtStart t) {
        Money amount = t.getValue().with(converter.at(t.getDateTime()));
        delta.subtract(amount);
        cost.add(amount);
    }

    @Override
    public void visit(CurrencyConverter converter, CalculationLineItem.ValuationAtEnd t) {
        delta.add(t.getValue().with(converter.at(t.getDateTime())));
    }

    @Override
    public void visit(CurrencyConverter converter, CalculationLineItem.DividendPayment t) {
        delta.add(t.getValue().with(converter.at(t.getDateTime())));
    }

    @Override
    public void visit(CurrencyConverter converter, CalculationLineItem.TransactionItem item, AccountTransaction t) {
        Type type = t.getType();
        switch (type) {
            case TAXES:
            case FEES:
                delta.subtract(t.getMonetaryAmount().with(converter.at(t.getDateTime())));
                break;
            case TAX_REFUND:
            case FEES_REFUND:
                delta.add(t.getMonetaryAmount().with(converter.at(t.getDateTime())));
                break;
            default:
                throw new IllegalArgumentException("unsupported type " + type); //$NON-NLS-1$
        }

    }

    @Override
    public void visit(CurrencyConverter converter, CalculationLineItem.TransactionItem item, PortfolioTransaction t) {
        com.aixohub.portfolio.model.PortfolioTransaction.Type type = t.getType();
        switch (type) {
            case BUY:
            case DELIVERY_INBOUND:
                Money amount = t.getMonetaryAmount().with(converter.at(t.getDateTime()));
                delta.subtract(amount);
                cost.add(amount);
                break;
            case SELL:
            case DELIVERY_OUTBOUND:
                delta.add(t.getMonetaryAmount().with(converter.at(t.getDateTime())));
                break;
            case TRANSFER_IN:
            case TRANSFER_OUT:
                // transferals do not contribute to the delta
                break;
            default:
                throw new UnsupportedOperationException("unsupported type " + type); //$NON-NLS-1$
        }
    }

    public Money getDelta() {
        return delta.toMoney();
    }

    public double getDeltaPercent() {
        if (delta.getAmount() == 0L && cost.getAmount() == 0L)
            return 0d;

        return delta.getAmount() / (double) cost.getAmount();
    }
}
