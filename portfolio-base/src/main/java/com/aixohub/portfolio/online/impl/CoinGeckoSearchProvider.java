package com.aixohub.portfolio.online.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.SecurityProperty;
import com.aixohub.portfolio.online.Factory;
import com.aixohub.portfolio.online.QuoteFeed;
import com.aixohub.portfolio.online.SecuritySearchProvider;
import com.aixohub.portfolio.online.impl.CoinGeckoQuoteFeed.Coin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoinGeckoSearchProvider implements SecuritySearchProvider {
    @Override
    public String getName() {
        return "CoinGecko"; //$NON-NLS-1$
    }

    @Override
    public List<ResultItem> search(String query, Type type) throws IOException {
        CoinGeckoQuoteFeed feed = Factory.getQuoteFeed(CoinGeckoQuoteFeed.class);

        List<ResultItem> items = new ArrayList<>();
        List<Coin> coins = feed.getCoins();

        for (Coin coin : coins) {
            if (coin.getName().contains(query) || coin.getId().contains(query))
                items.add(new Result(coin));
        }

        return items;
    }

    @Override
    public List<ResultItem> getCoins() throws IOException {
        CoinGeckoQuoteFeed feed = Factory.getQuoteFeed(CoinGeckoQuoteFeed.class);
        return feed.getCoins().stream().map(coin -> (ResultItem) new Result(coin)).toList();
    }

    static class Result implements ResultItem {
        private final Coin coin;

        /* package */ Result(Coin coin) {
            this.coin = coin;
        }

        @Override
        public String getSymbol() {
            return coin.getSymbol().toUpperCase();
        }

        @Override
        public String getName() {
            return coin.getName();
        }

        @Override
        public String getType() {
            return Messages.LabelCryptocurrency;
        }

        @Override
        public String getExchange() {
            return coin.getId();
        }

        @Override
        public String getIsin() {
            return null;
        }

        @Override
        public String getWkn() {
            return null;
        }

        @Override
        public String getSource() {
            return "CoinGecko"; //$NON-NLS-1$
        }

        @Override
        public Security create(Client client) {
            Security security = new Security(coin.getName(), client.getBaseCurrency());
            security.setTickerSymbol(coin.getSymbol().toUpperCase());
            security.setFeed(CoinGeckoQuoteFeed.ID);
            security.setPropertyValue(SecurityProperty.Type.FEED, CoinGeckoQuoteFeed.COINGECKO_COIN_ID, coin.getId());
            security.setLatestFeed(QuoteFeed.MANUAL);
            return security;
        }
    }
}
