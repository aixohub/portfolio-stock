package com.aixohub.portfolio.snapshot.security;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.Transaction;
import com.aixohub.portfolio.model.Transaction.Unit;
import com.aixohub.portfolio.model.TransactionOwner;
import com.aixohub.portfolio.model.TransactionPair;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.money.MoneyCollectors;
import com.aixohub.portfolio.money.Values;
import com.aixohub.portfolio.snapshot.SecurityPosition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

public interface CalculationLineItem {
    Comparator<CalculationLineItem> BY_DATE = new Comparator<CalculationLineItem>() {
        @Override
        public int compare(CalculationLineItem l1, CalculationLineItem l2) {
            int compareTo = l1.getDateTime().compareTo(l2.getDateTime());
            if (compareTo != 0)
                return compareTo;

            // Fallback
            return Long.compare(l1.getOrderingHint(), l2.getOrderingHint());
        }
    };

    static CalculationLineItem of(TransactionPair<?> transaction) {
        boolean isDividendPayment = transaction.getTransaction() instanceof AccountTransaction
                && ((AccountTransaction) transaction.getTransaction())
                .getType() == AccountTransaction.Type.DIVIDENDS;

        return isDividendPayment ? new DividendPayment(transaction) : new TransactionItem(transaction);
    }

    static CalculationLineItem of(Portfolio portfolio, PortfolioTransaction transaction) {
        return of(new TransactionPair<>(portfolio, transaction));
    }

    static CalculationLineItem of(Account account, AccountTransaction transaction) {
        return of(new TransactionPair<>(account, transaction));
    }

    static CalculationLineItem atStart(Portfolio portfolio, SecurityPosition position, LocalDateTime date) {
        return new ValuationAtStart(portfolio, position, date);
    }

    static CalculationLineItem atEnd(Portfolio portfolio, SecurityPosition position, LocalDateTime date) {
        return new ValuationAtEnd(portfolio, position, date);
    }

    TransactionOwner<?> getOwner();

    String getLabel();

    LocalDateTime getDateTime();

    long getOrderingHint();

    Money getValue();

    default Optional<Transaction> getTransaction() {
        return Optional.empty();
    }

    default Optional<SecurityPosition> getSecurityPosition() {
        return Optional.empty();
    }

    class TransactionItem implements CalculationLineItem {
        private final TransactionPair<?> txPair;

        private TransactionItem(TransactionPair<?> transaction) {
            this.txPair = transaction;
        }

        @Override
        public TransactionOwner<?> getOwner() {
            return txPair.getOwner();
        }

        @Override
        public String getLabel() {
            if (txPair.getTransaction() instanceof AccountTransaction)
                return ((AccountTransaction) txPair.getTransaction()).getType().toString();
            else if (txPair.getTransaction() instanceof PortfolioTransaction)
                return ((PortfolioTransaction) txPair.getTransaction()).getType().toString();
            else
                return null;
        }

        @Override
        public LocalDateTime getDateTime() {
            return txPair.getTransaction().getDateTime();
        }

        @Override
        public long getOrderingHint() {
            return txPair.getTransaction().getUpdatedAt().getEpochSecond();
        }

        @Override
        public Money getValue() {
            return txPair.getTransaction().getMonetaryAmount();
        }

        @Override
        public Optional<Transaction> getTransaction() {
            return Optional.of(txPair.getTransaction());
        }

        protected Transaction tx() {
            return txPair.getTransaction();
        }

    }

    class DividendPayment extends TransactionItem {
        private long totalShares;
        private Money fifoCost;
        private Money movingAverageCost;

        private DividendPayment(TransactionPair<?> transaction) {
            super(transaction);
        }

        static long amountFractionPerShare(long amount, long shares) {
            if (shares == 0)
                return 0;

            return BigDecimal.valueOf(amount) //
                    .movePointLeft(Values.Amount.precision()) //
                    .movePointRight(Values.AmountFraction.precision()) //
                    .movePointRight(Values.Share.precision()) //
                    .divide(BigDecimal.valueOf(shares), Values.MC) //
                    .setScale(0, RoundingMode.HALF_EVEN).longValue();
        }

        public long getDividendPerShare() {
            return amountFractionPerShare(getGrossValueAmount(), tx().getShares());
        }

        /**
         * Returns the FIFO costs. It is the cost of the total position of the
         * given security. However, a dividend payment may only be about partial
         * holdings, for example if the security is held in multiple securities
         * accounts.
         */
        /* package */ Money getFifoCost() {
            return fifoCost;
        }

        /* package */ void setFifoCost(Money fifoCost) {
            this.fifoCost = fifoCost;
        }

        /**
         * Returns the costs based on moving average. It is the cost of the
         * total position of the given security. However, a dividend payment may
         * only be about partial holdings, for example if the security is held
         * in multiple securities accounts.
         */
        /* package */ Money getMovingAverageCost() {
            return movingAverageCost;
        }

        /* package */ void setMovingAverageCost(Money movingAverageCost) {
            this.movingAverageCost = movingAverageCost;
        }

        /* package */ void setTotalShares(long totalShares) {
            this.totalShares = totalShares;
        }

        public double getPersonalDividendYield() {
            if ((fifoCost == null) || (fifoCost.getAmount() <= 0))
                return 0;

            double cost = fifoCost.getAmount();

            if (tx().getShares() > 0)
                cost = fifoCost.getAmount() * (tx().getShares() / (double) totalShares);

            return getGrossValueAmount() / cost;
        }

        public double getPersonalDividendYieldMovingAverage() {
            if ((movingAverageCost == null) || (movingAverageCost.getAmount() <= 0))
                return 0;

            double cost = movingAverageCost.getAmount();

            if (tx().getShares() > 0)
                cost = movingAverageCost.getAmount() * (tx().getShares() / (double) totalShares);

            return getGrossValueAmount() / cost;
        }

        public long getGrossValueAmount() {
            long taxes = tx().getUnits().filter(u -> u.getType() == Unit.Type.TAX)
                    .collect(MoneyCollectors.sum(tx().getCurrencyCode(), Unit::getAmount)).getAmount();

            return tx().getAmount() + taxes;
        }

        public Money getGrossValue() {
            return Money.of(tx().getCurrencyCode(), getGrossValueAmount());
        }
    }

    abstract class Valuation implements CalculationLineItem {
        private final Portfolio portfolio;
        private final SecurityPosition position;
        private final LocalDateTime date;

        protected Valuation(Portfolio portfolio, SecurityPosition position, LocalDateTime date) {
            this.portfolio = portfolio;
            this.position = position;
            this.date = date;
        }

        @Override
        public TransactionOwner<?> getOwner() {
            return portfolio;
        }

        @Override
        public String getLabel() {
            return Messages.LabelQuotation;
        }

        @Override
        public LocalDateTime getDateTime() {
            return date;
        }

        @Override
        public Money getValue() {
            return position.calculateValue();
        }

        @Override
        public Optional<SecurityPosition> getSecurityPosition() {
            return Optional.of(position);
        }
    }

    class ValuationAtStart extends Valuation {
        private ValuationAtStart(Portfolio portfolio, SecurityPosition position, LocalDateTime date) {
            super(portfolio, position, date);
        }

        @Override
        public long getOrderingHint() {
            return 0;
        }

    }

    class ValuationAtEnd extends Valuation {
        private ValuationAtEnd(Portfolio portfolio, SecurityPosition position, LocalDateTime date) {
            super(portfolio, position, date);
        }

        @Override
        public long getOrderingHint() {
            return Long.MAX_VALUE;
        }
    }
}
