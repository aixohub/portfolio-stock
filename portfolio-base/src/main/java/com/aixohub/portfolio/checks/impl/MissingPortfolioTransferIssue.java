package com.aixohub.portfolio.checks.impl;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.checks.QuickFix;
import com.aixohub.portfolio.model.Client;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.PortfolioTransferEntry;
import com.aixohub.portfolio.money.Values;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/* package */class MissingPortfolioTransferIssue extends AbstractPortfolioIssue {
    public MissingPortfolioTransferIssue(Client client, Portfolio portfolio, PortfolioTransaction transaction) {
        super(client, portfolio, transaction);
    }

    @Override
    public String getLabel() {
        return MessageFormat.format(Messages.IssueMissingPortfolioTransfer, //
                transaction.getType().toString(), //
                Values.Share.format(transaction.getShares()), //
                Values.Quote.format(transaction.getGrossPricePerShare()), //
                transaction.getSecurity().getName());
    }

    @Override
    public List<QuickFix> getAvailableFixes() {
        List<QuickFix> answer = client.getPortfolios().stream() //
                .filter(p -> !p.equals(portfolio)) //
                .map(p -> new CreateTransferFix(p)) //
                .collect(Collectors.toList());

        answer.add(new DeleteTransactionFix<PortfolioTransaction>(client, portfolio, transaction));

        return answer;
    }

    private final class CreateTransferFix implements QuickFix {
        private final Portfolio crossPortfolio;

        private CreateTransferFix(Portfolio crossPortfolio) {
            this.crossPortfolio = crossPortfolio;
        }

        @Override
        public String getLabel() {
            return MessageFormat.format(Messages.FixCreateTransfer, crossPortfolio.getName());
        }

        @Override
        public String getDoneLabel() {
            PortfolioTransaction.Type target;
            if (transaction.getType() == PortfolioTransaction.Type.TRANSFER_IN)
                target = PortfolioTransaction.Type.TRANSFER_OUT;
            else
                target = PortfolioTransaction.Type.TRANSFER_IN;

            return MessageFormat.format(Messages.FixCreateTransferDone, target.toString());
        }

        @Override
        public void execute() {
            Portfolio from;
            Portfolio to;

            if (transaction.getType() == PortfolioTransaction.Type.TRANSFER_IN) {
                from = crossPortfolio;
                to = portfolio;
            } else {
                from = portfolio;
                to = crossPortfolio;
            }

            PortfolioTransferEntry entry = new PortfolioTransferEntry(from, to);
            entry.setDate(transaction.getDateTime());
            entry.setSecurity(transaction.getSecurity());
            entry.setShares(transaction.getShares());
            entry.setAmount(transaction.getAmount());
            entry.setCurrencyCode(transaction.getCurrencyCode());
            entry.insert();

            portfolio.getTransactions().remove(transaction);
        }
    }

}
