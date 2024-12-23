package com.aixohub.portfolio.online.impl;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.SecurityEvent.DividendEvent;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.money.Values;
import com.aixohub.portfolio.online.DividendFeed;
import com.aixohub.portfolio.util.WebAccess;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.osgi.framework.FrameworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DivvyDiaryDividendFeed implements DividendFeed {
    @VisibleForTesting
    PageCache<String> cache = new PageCache<>();
    private String apiKey;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DividendEvent> getDividendPayments(Security security) throws IOException {
        if (apiKey == null)
            return Collections.emptyList();

        if (Strings.isNullOrEmpty(security.getIsin()))
            return Collections.emptyList();

        String json = cache.lookup(security.getIsin());
        if (json == null) {
            json = createWebAccess("api.divvydiary.com", "/symbols/" + security.getIsin()) //$NON-NLS-1$ //$NON-NLS-2$
                    .addHeader("X-API-Key", apiKey) //$NON-NLS-1$
                    .addUserAgent("PortfolioPerformance/" //$NON-NLS-1$
                            + FrameworkUtil.getBundle(DivvyDiaryDividendFeed.class).getVersion()
                            .toString())
                    .get();
        }

        JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
        if (jsonObject == null) {
            throw new IOException("server returned data that doesn't seem to be JSON"); //$NON-NLS-1$
        }

        JSONArray dividends = (JSONArray) jsonObject.get("dividends"); //$NON-NLS-1$
        if (dividends == null) {
            throw new IOException("server returned an unexpected JSON-format"); //$NON-NLS-1$
        }

        List<DividendEvent> answer = new ArrayList<>();

        dividends.forEach(entry -> {
            JSONObject row = (JSONObject) entry;

            DividendEvent payment = new DividendEvent();

            payment.setDate(YahooHelper.fromISODate((String) row.get("exDate"))); //$NON-NLS-1$
            payment.setPaymentDate(YahooHelper.fromISODate((String) row.get("payDate"))); //$NON-NLS-1$
            payment.setAmount(Money.of((String) row.get("currency"), //$NON-NLS-1$
                    Values.Amount.factorize(((Number) row.get("amount")).doubleValue()))); //$NON-NLS-1$

            payment.setSource("divvydiary.com"); //$NON-NLS-1$

            answer.add(payment);
        });

        // add it to the cache only if it was successfully parsed, otherwise we
        // might cache invalid data
        cache.computeIfAbsent(security.getIsin(), json);

        return answer;
    }

    @VisibleForTesting
    WebAccess createWebAccess(String host, String path) {
        return new WebAccess(host, path);
    }
}
