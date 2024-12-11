package com.aixohub.portfolio.snapshot;

import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.snapshot.filter.ReadOnlyAccount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AccountSnapshot {
    // //////////////////////////////////////////////////////////////
    // factory methods
    // //////////////////////////////////////////////////////////////

    private final Account account;

    // //////////////////////////////////////////////////////////////
    // instance impl
    // //////////////////////////////////////////////////////////////
    private final LocalDate date;
    private final CurrencyConverter converter;
    private final Money funds;
    private AccountSnapshot(Account account, LocalDate date, CurrencyConverter converter, Money funds) {
        this.account = account;
        this.date = date;
        this.converter = converter;
        this.funds = funds;
    }

    public static AccountSnapshot create(Account account, CurrencyConverter converter, LocalDate date) {
        long funds = 0;

        LocalDateTime reference = date.atTime(LocalTime.MAX);

        for (AccountTransaction t : account.getTransactions()) {
            if (!t.getDateTime().isAfter(reference)) {
                if (t.getType().isDebit())
                    funds -= t.getAmount();
                else
                    funds += t.getAmount();
            }
        }

        return new AccountSnapshot(account, date, converter, Money.of(account.getCurrencyCode(), funds));
    }

    /* package */ Account unwrapAccount() {
        return account instanceof ReadOnlyAccount readOnly ? readOnly.unwrap() : account;
    }

    public Account getAccount() {
        return account;
    }

    public LocalDate getTime() {
        return date;
    }

    public CurrencyConverter getCurrencyConverter() {
        return converter;
    }

    public Money getFunds() {
        return funds.with(converter.at(date));
    }

    public Money getUnconvertedFunds() {
        return funds;
    }
}
