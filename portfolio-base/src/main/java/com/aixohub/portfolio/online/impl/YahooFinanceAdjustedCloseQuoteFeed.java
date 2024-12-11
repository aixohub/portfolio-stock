package com.aixohub.portfolio.online.impl;

import com.aixohub.portfolio.Messages;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

public class YahooFinanceAdjustedCloseQuoteFeed extends YahooFinanceQuoteFeed {
    @Override
    public String getId() {
        return "YAHOO-ADJUSTEDCLOSE"; //$NON-NLS-1$
    }

    @Override
    public String getName() {
        return Messages.LabelYahooFinanceAdjustedClose;
    }

    @Override
    protected JSONArray extractQuotesArray(JSONObject indicators) throws IOException {
        JSONArray quotes = (JSONArray) indicators.get("adjclose"); //$NON-NLS-1$
        if (quotes == null || quotes.isEmpty())
            throw new IOException();

        JSONObject quote = (JSONObject) quotes.get(0);
        if (quote == null)
            throw new IOException();

        JSONArray adjclose = (JSONArray) quote.get("adjclose"); //$NON-NLS-1$
        if (adjclose == null || adjclose.isEmpty())
            throw new IOException();

        return adjclose;
    }
}
