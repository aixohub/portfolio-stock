package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.BuySellEntry;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.money.Values;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/* package */class MissingBuySellPortfolioIssue extends AbstractAccountIssue {
    public MissingBuySellPortfolioIssue(Client client, Account account, AccountTransaction transaction) {
        super(client, account, transaction);
    }

    @Override
    public String getLabel() {
        return MessageFormat.format(Messages.IssueMissingBuySellInPortfolio, transaction.getType().toString(),
                transaction.getSecurity().getName());
    }

    @Override
    public List<QuickFix> getAvailableFixes() {
        List<QuickFix> answer = new ArrayList<QuickFix>();
        answer.add(new DeleteTransactionFix<AccountTransaction>(client, account, transaction));
        client.getPortfolios().stream().forEach(p -> answer.add(new CreateBuySellEntryFix(p)));
        return answer;
    }

    private final class CreateBuySellEntryFix implements QuickFix {
        private final Portfolio portfolio;

        private CreateBuySellEntryFix(Portfolio portfolio) {
            this.portfolio = portfolio;
        }

        @Override
        public String getLabel() {
            return MessageFormat.format(Messages.FixCreateCrossEntryPortfolio, portfolio.getName());
        }

        @Override
        public String getDoneLabel() {
            return MessageFormat.format(Messages.FixCreateCrossEntryDone, transaction.getType().toString());
        }

        @Override
        public void execute() {
            BuySellEntry entry = new BuySellEntry(portfolio, account);
            entry.setDate(transaction.getDateTime());
            entry.setType(PortfolioTransaction.Type.valueOf(transaction.getType().name()));
            entry.setSecurity(transaction.getSecurity());
            entry.setShares(Values.Share.factor());
            entry.setAmount(transaction.getAmount());
            entry.setCurrencyCode(transaction.getCurrencyCode());
            entry.insert();

            account.getTransactions().remove(transaction);
        }
    }
}
