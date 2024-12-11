package com.aixohub.portfolio.online.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.online.QuoteFeed;
import com.aixohub.portfolio.online.QuoteFeedData;

public final class ManualQuoteFeed implements QuoteFeed {
    @Override
    public String getId() {
        return QuoteFeed.MANUAL;
    }

    @Override
    public String getName() {
        return Messages.QuoteFeedManual;
    }

    @Override
    public QuoteFeedData getHistoricalQuotes(Security security, boolean collectRawResponse) {
        return new QuoteFeedData();
    }
}
