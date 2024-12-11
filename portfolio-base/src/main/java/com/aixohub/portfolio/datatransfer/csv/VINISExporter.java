package com.aixohub.portfolio.datatransfer.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVPrinter;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.money.CurrencyConverter;
import com.aixohub.portfolio.money.CurrencyConverterImpl;
import com.aixohub.portfolio.money.ExchangeRateProviderFactory;
import com.aixohub.portfolio.money.MonetaryOperator;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.money.MoneyCollectors;
import com.aixohub.portfolio.money.MutableMoney;
import com.aixohub.portfolio.money.Values;
import com.aixohub.portfolio.snapshot.AccountSnapshot;
import com.aixohub.portfolio.snapshot.AssetPosition;
import com.aixohub.portfolio.snapshot.ClientPerformanceSnapshot;
import com.aixohub.portfolio.snapshot.ClientPerformanceSnapshot.CategoryType;
import com.aixohub.portfolio.snapshot.ReportingPeriod;
import com.aixohub.portfolio.snapshot.security.SecurityPerformanceIndicator;
import com.aixohub.portfolio.snapshot.security.SecurityPerformanceRecord;
import com.aixohub.portfolio.snapshot.security.SecurityPerformanceSnapshot;
import com.aixohub.portfolio.util.Interval;


/**
 * Special exporter for the VINIS-App
 */
public class VINISExporter {
    /**
     * Export all values in 'VINIS-App' Format
     */
    public void exportAllValues(File file, Client client, ExchangeRateProviderFactory factory) throws IOException {
        final String baseCurrency = client.getBaseCurrency();
        CurrencyConverter converter = new CurrencyConverterImpl(factory, baseCurrency);

        LocalDate lastYear = LocalDate.now().minusYears(1);
        LocalDate firstYear = LocalDate.now().minusYears(100);

        ReportingPeriod periodCurrentYear = new ReportingPeriod.YearToDate();
        ReportingPeriod periodLastYear = new ReportingPeriod.YearX(lastYear.getYear());
        ReportingPeriod periodAllYears = new ReportingPeriod.FromXtoY(
                firstYear.with(TemporalAdjusters.firstDayOfYear()), LocalDate.now());

        ClientPerformanceSnapshot performanceAllYears = new ClientPerformanceSnapshot(client, converter,
                periodAllYears.toInterval(LocalDate.now()));
        ClientPerformanceSnapshot performanceCurrentYear = new ClientPerformanceSnapshot(client, converter,
                periodCurrentYear.toInterval(LocalDate.now()));
        ClientPerformanceSnapshot performanceLastYear = new ClientPerformanceSnapshot(client, converter,
                periodLastYear.toInterval(LocalDate.now()));

        Money earningsCurrentYear = performanceCurrentYear.getValue(ClientPerformanceSnapshot.CategoryType.EARNINGS);
        Money earningsLastYear = performanceLastYear.getValue(ClientPerformanceSnapshot.CategoryType.EARNINGS);
        Money earningsAll = performanceAllYears.getValue(ClientPerformanceSnapshot.CategoryType.EARNINGS);

        Money capitalGainsCurrentYear = performanceCurrentYear.getValue(ClientPerformanceSnapshot.CategoryType.CAPITAL_GAINS);
        Money capitalGainsLastYear = performanceLastYear.getValue(ClientPerformanceSnapshot.CategoryType.CAPITAL_GAINS);
        Money capitalGainsAll = performanceAllYears.getValue(ClientPerformanceSnapshot.CategoryType.CAPITAL_GAINS);

        Money realizedCapitalGainsCurrentYear = performanceCurrentYear.getValue(CategoryType.REALIZED_CAPITAL_GAINS);
        Money realizedCapitalGainsLastYear = performanceLastYear.getValue(CategoryType.REALIZED_CAPITAL_GAINS);
        Money realizedCapitalGainsAll = performanceAllYears.getValue(CategoryType.REALIZED_CAPITAL_GAINS);

        MutableMoney buySecurityValue = MutableMoney.of(baseCurrency);
        MutableMoney currentSecurityValue = MutableMoney.of(baseCurrency);
        MutableMoney buyTotalValue = MutableMoney.of(baseCurrency);
        MutableMoney currentTotalValue = MutableMoney.of(baseCurrency);

        SecurityPerformanceSnapshot securityPerformance = SecurityPerformanceSnapshot.create(client, converter,
                Interval.of(LocalDate.MIN, LocalDate.now()), SecurityPerformanceIndicator.Costs.class);

        List<AssetPosition> assets = performanceCurrentYear.getEndClientSnapshot().getAssetPositions()
                .collect(Collectors.toList());

        MonetaryOperator toBaseCurrency = converter.at(LocalDate.now());

        for (AssetPosition asset : assets) {
            Money valuation = asset.getValuation().with(toBaseCurrency);

            if (asset.getSecurity() != null) {
                Money fifo = securityPerformance.getRecord(asset.getSecurity())
                        .map(SecurityPerformanceRecord::getFifoCost).orElse(Money.of(baseCurrency, 0))
                        .with(toBaseCurrency);
                buySecurityValue.add(fifo);
                currentSecurityValue.add(valuation);
                buyTotalValue.add(fifo);
            } else {
                buyTotalValue.add(valuation);
            }

            currentTotalValue.add(valuation);
        }

        Money cash = performanceCurrentYear.getEndClientSnapshot().getAccounts().stream().map(AccountSnapshot::getFunds)
                .collect(MoneyCollectors.sum(baseCurrency));

        // write to file
        try (CSVPrinter printer = new CSVPrinter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8),
                CSVExporter.STRATEGY)) {
            writeHeader(printer);

            write(printer, Messages.VINISAppValueFundsSum, cash);

            write(printer, Messages.VINISAppValueSecuritiesPurchase, buySecurityValue.toMoney());
            write(printer, Messages.VINISAppValueSecuritiesMarket, currentSecurityValue.toMoney());

            write(printer, Messages.VINISAppValueTotalAssetsPurchase, buyTotalValue.toMoney());
            write(printer, Messages.VINISAppValueTotalAssetsMarket, currentTotalValue.toMoney());

            write(printer, Messages.VINISAppValueEarningsCurrentYear, earningsCurrentYear);
            write(printer, Messages.VINISAppValueEarningsLastYear, earningsLastYear);
            write(printer, Messages.VINISAppValueEarningsTotal, earningsAll);

            write(printer, Messages.VINISAppValueCapitalGainsCurrentYear, capitalGainsCurrentYear);
            write(printer, Messages.VINISAppValueCapitalGainsLastYear, capitalGainsLastYear);
            write(printer, Messages.VINISAppValueCapitalGainsTotal, capitalGainsAll);

            write(printer, Messages.VINISAppValueRealizedCapitalGainsCurrentYear, realizedCapitalGainsCurrentYear);
            write(printer, Messages.VINISAppValueRealizedCapitalGainsLastYear, realizedCapitalGainsLastYear);
            write(printer, Messages.VINISAppValueRealizedCapitalGainsTotal, realizedCapitalGainsAll);
        }
    }

    private void write(CSVPrinter printer, String description, Money value) throws IOException {
        printer.print(description);
        printer.print(Values.Amount.format(value.getAmount()));
        printer.print(value.getCurrencyCode());
        printer.println();
    }

    private void writeHeader(CSVPrinter printer) throws IOException {
        printer.print(Messages.CSVColumn_Name);
        printer.print(Messages.CSVColumn_Value);
        printer.print(Messages.CSVColumn_Currency);
        printer.println();
    }
}
