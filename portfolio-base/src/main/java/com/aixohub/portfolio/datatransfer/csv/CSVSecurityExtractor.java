package com.aixohub.portfolio.datatransfer.csv;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.datatransfer.csv.CSVImporter.AmountField;
import com.aixohub.portfolio.datatransfer.csv.CSVImporter.Column;
import com.aixohub.portfolio.datatransfer.csv.CSVImporter.DateField;
import com.aixohub.portfolio.datatransfer.csv.CSVImporter.Field;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.online.impl.YahooFinanceQuoteFeed;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/* package */class CSVSecurityExtractor extends BaseCSVExtractor {
    /* package */ CSVSecurityExtractor(Client client) {
        super(client, Messages.CSVDefSecurities);

        List<Field> fields = getFields();
        fields.add(new Field("isin", Messages.CSVColumn_ISIN).setOptional(true)); //$NON-NLS-1$
        fields.add(new Field("wkn", Messages.CSVColumn_WKN).setOptional(true)); //$NON-NLS-1$
        fields.add(new Field("ticker", Messages.CSVColumn_TickerSymbol).setOptional(true)); //$NON-NLS-1$
        fields.add(new Field("name", Messages.CSVColumn_SecurityName).setOptional(true)); //$NON-NLS-1$
        fields.add(new Field("currency", Messages.CSVColumn_Currency).setOptional(true)); //$NON-NLS-1$
        fields.add(new Field("note", Messages.CSVColumn_Note).setOptional(true)); //$NON-NLS-1$

        fields.add(new DateField("date", Messages.CSVColumn_DateQuote).setOptional(true)); //$NON-NLS-1$
        fields.add(new AmountField("quote", Messages.CSVColumn_Quote, "Schluss", "Schlusskurs", "Close") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                .setOptional(true));
    }

    @Override
    public String getCode() {
        return "investment-vehicle"; //$NON-NLS-1$
    }

    @Override
    void extract(List<Item> items, String[] rawValues, Map<String, Column> field2column) throws ParseException {
        // check if we can identify a security
        Security security = getSecurity(rawValues, field2column, s -> {
            s.setCurrencyCode(getCurrencyCode(Messages.CSVColumn_Currency, rawValues, field2column));

            String note = getText(Messages.CSVColumn_Note, rawValues, field2column);
            s.setNote(note);

            if (s.getTickerSymbol() != null)
                s.setFeed(YahooFinanceQuoteFeed.ID);

            items.add(new SecurityItem(s));
        });

        if (security == null)
            throw new ParseException(MessageFormat.format(Messages.CSVImportMissingSecurity,
                    new StringJoiner(", ").add(Messages.CSVColumn_ISIN) //$NON-NLS-1$
                            .add(Messages.CSVColumn_TickerSymbol).add(Messages.CSVColumn_WKN)
                            .toString()),
                    0);

        // nothing to do to add the security: if necessary, the security item
        // has been created in the callback of the #extractSecurity method

        // check if the data contains price

        getSecurityPrice(Messages.CSVColumn_DateQuote, rawValues, field2column)
                .ifPresent(price -> items.add(new SecurityPriceItem(security, price)));
    }
}
