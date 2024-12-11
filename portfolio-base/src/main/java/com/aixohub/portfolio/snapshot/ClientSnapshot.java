package com.aixohub.portfolio.snapshot;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.InvestmentVehicle;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.Taxonomy;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.money.MutableMoney;
import com.aixohub.portfolio.snapshot.filter.ReadOnlyAccount;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientSnapshot {
    private final CurrencyConverter converter;
    private final LocalDate date;

    private final List<AccountSnapshot> accounts = new ArrayList<>();
    private final List<PortfolioSnapshot> portfolios = new ArrayList<>();

    private PortfolioSnapshot jointPortfolio;
    private Money assets;

    private ClientSnapshot(CurrencyConverter converter, LocalDate date) {
        this.converter = converter;
        this.date = date;
    }

    public static ClientSnapshot create(Client client, CurrencyConverter converter, LocalDate date) {
        ClientSnapshot snapshot = new ClientSnapshot(converter, date);

        for (Account account : client.getAccounts())
            snapshot.accounts.add(AccountSnapshot.create(account, converter, date));

        for (Portfolio portfolio : client.getPortfolios())
            snapshot.portfolios.add(PortfolioSnapshot.create(portfolio, converter, date));

        return snapshot;
    }

    public String getCurrencyCode() {
        return converter.getTermCurrency();
    }

    public CurrencyConverter getCurrencyConverter() {
        return converter;
    }

    public LocalDate getTime() {
        return date;
    }

    public List<AccountSnapshot> getAccounts() {
        return accounts;
    }

    public List<PortfolioSnapshot> getPortfolios() {
        return portfolios;
    }

    public PortfolioSnapshot getJointPortfolio() {
        if (this.jointPortfolio == null) {
            if (portfolios.isEmpty()) {
                Portfolio portfolio = new Portfolio();
                portfolio.setName(Messages.LabelJointPortfolio);
                portfolio.setReferenceAccount(new Account(Messages.LabelJointPortfolio));
                this.jointPortfolio = PortfolioSnapshot.create(portfolio, converter, date);
            } else if (portfolios.size() == 1) {
                this.jointPortfolio = portfolios.get(0);
            } else {
                this.jointPortfolio = PortfolioSnapshot.merge(portfolios, converter);
            }
        }

        return this.jointPortfolio;
    }

    public Money getMonetaryAssets() {
        if (this.assets == null) {
            MutableMoney sum = MutableMoney.of(getCurrencyCode());

            for (AccountSnapshot account : accounts)
                sum.add(account.getFunds());

            // use joint portfolio to reduce rounding errors if a security is
            // split across multiple portfolio
            sum.add(getJointPortfolio().getValue());

            this.assets = sum.toMoney();
        }

        return this.assets;
    }

    public GroupByTaxonomy groupByTaxonomy(Taxonomy taxonomy) {
        return new GroupByTaxonomy(taxonomy, this);
    }

    public Map<InvestmentVehicle, AssetPosition> getPositionsByVehicle() {
        return getAssetPositions().collect(Collectors.toMap(p -> {
            InvestmentVehicle v = p.getInvestmentVehicle();
            if (v instanceof ReadOnlyAccount readOnly)
                return readOnly.unwrap();
            else
                return v;
        }, v -> v));
    }

    public Stream<AssetPosition> getAssetPositions() {
        List<AssetPosition> answer = new ArrayList<>();

        Money monetaryAssets = getMonetaryAssets();

        for (SecurityPosition p : getJointPortfolio().getPositions())
            answer.add(new AssetPosition(p, converter, date, monetaryAssets));

        for (AccountSnapshot a : accounts)
            answer.add(new AssetPosition(new SecurityPosition(a), converter, date, monetaryAssets));

        return answer.stream();
    }
}
