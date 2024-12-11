package com.aixohub.portfolio.datatransfer.csv;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.datatransfer.csv.CSVImporter.AmountField;
import com.aixohub.portfolio.datatransfer.csv.CSVImporter.Column;
import com.aixohub.portfolio.datatransfer.csv.CSVImporter.DateField;
import com.aixohub.portfolio.datatransfer.csv.CSVImporter.Field;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.SecurityPrice;

import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* package */class CSVSecurityPriceExtractor extends CSVExtractor {
    private final List<Field> fields;

    /* package */ CSVSecurityPriceExtractor() {
        fields = new ArrayList<>();
        fields.add(new DateField("date", Messages.CSVColumn_Date)); //$NON-NLS-1$
        fields.add(new AmountField("quote", Messages.CSVColumn_Quote, "Schluss", "Schlusskurs", "Close")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    @Override
    public String getCode() {
        return "investment-vehicle-price"; //$NON-NLS-1$
    }

    @Override
    public String getLabel() {
        return Messages.CSVDefHistoricalQuotes;
    }

    @Override
    public List<Field> getFields() {
        return fields;
    }

    @Override
    public List<Item> extract(int skipLines, List<String[]> rawValues, Map<String, Column> field2column,
                              List<Exception> errors) {
        Security dummy = new Security(null, null);

        for (String[] line : rawValues) {
            try {
                SecurityPrice p = extract(line, field2column);
                if (p.getValue() >= 0)
                    dummy.addPrice(p);
            } catch (ParseException e) {
                errors.add(e);
            }
        }

        List<Item> result = new ArrayList<>();
        if (!dummy.getPrices().isEmpty())
            result.add(new SecurityItem(dummy));
        return result;
    }

    private SecurityPrice extract(String[] rawValues, Map<String, Column> field2column) throws ParseException {
        LocalDateTime date = getDate(Messages.CSVColumn_Date, null, rawValues, field2column);
        if (date == null)
            throw new ParseException(MessageFormat.format(Messages.CSVImportMissingField, Messages.CSVColumn_Date), 0);

        Long amount = getQuote(Messages.CSVColumn_Quote, rawValues, field2column);
        if (amount == null)
            throw new ParseException(MessageFormat.format(Messages.CSVImportMissingField, Messages.CSVColumn_Quote), 0);

        return new SecurityPrice(date.toLocalDate(), Math.abs(amount));
    }
}
