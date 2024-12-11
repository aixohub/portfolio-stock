package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.Check;
import com.aixohub.portfolio.checks.Issue;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.money.CurrencyUnit;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Checks if there is at least one account or security without a currency.
 */
public class MissingCurrencyCheck implements Check {
    @Override
    public List<Issue> execute(Client client) {
        boolean hasCurrencyMissing = false;

        for (Account account : client.getAccounts())
            if (account.getCurrencyCode() == null) {
                hasCurrencyMissing = true;
                break;
            }

        if (!hasCurrencyMissing) {
            for (Security security : client.getSecurities())
                if (security.getCurrencyCode() == null && security.hasTransactions(client))
                    hasCurrencyMissing = true;
        }

        if (hasCurrencyMissing) {
            Issue issue = new MissingCurrencyIssue(client);
            return List.of(issue);
        } else {
            return Collections.emptyList();
        }
    }

    public class CurrencyQuickFix implements QuickFix {
        private final Client client;
        private final CurrencyUnit currency;

        public CurrencyQuickFix(Client client, CurrencyUnit currency) {
            if (currency == null)
                throw new NullPointerException();

            this.client = client;
            this.currency = currency;
        }

        @Override
        public String getLabel() {
            return currency.getLabel();
        }

        @Override
        public String getDoneLabel() {
            return MessageFormat.format(Messages.FixAssignCurrencyCodeDone, currency.getDisplayName());
        }

        @Override
        public void execute() {
            for (Account account : client.getAccounts())
                if (account.getCurrencyCode() == null)
                    account.setCurrencyCode(currency.getCurrencyCode());

            for (Security security : client.getSecurities())
                if (security.getCurrencyCode() == null && security.hasTransactions(client))
                    security.setCurrencyCode(currency.getCurrencyCode());
        }
    }

    private final class MissingCurrencyIssue implements Issue {
        private final Client client;

        public MissingCurrencyIssue(Client client) {
            this.client = client;
        }

        @Override
        public LocalDate getDate() {
            return null;
        }

        @Override
        public Object getEntity() {
            return client;
        }

        @Override
        public Long getAmount() {
            return null;
        }

        @Override
        public String getLabel() {
            return Messages.IssueMissingCurrencyCode;
        }

        @Override
        public List<QuickFix> getAvailableFixes() {
            List<QuickFix> fixes = new ArrayList<QuickFix>();

            // add most likely currencies at the top of the list
            fixes.add(new CurrencyQuickFix(client, CurrencyUnit.getInstance("EUR"))); //$NON-NLS-1$
            fixes.add(new CurrencyQuickFix(client, CurrencyUnit.getInstance("CHF"))); //$NON-NLS-1$
            fixes.add(new CurrencyQuickFix(client, CurrencyUnit.getInstance("USD"))); //$NON-NLS-1$

            fixes.add(QuickFix.SEPARATOR);

            List<CurrencyUnit> available = CurrencyUnit.getAvailableCurrencyUnits();
            Collections.sort(available);
            for (CurrencyUnit currency : available)
                fixes.add(new CurrencyQuickFix(client, currency));

            return fixes;
        }
    }
}
