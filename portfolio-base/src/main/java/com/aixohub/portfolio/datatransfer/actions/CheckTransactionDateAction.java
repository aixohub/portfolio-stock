package com.aixohub.portfolio.datatransfer.actions;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.datatransfer.ImportAction;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransferEntry;
import com.aixohub.portfolio.model.BuySellEntry;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.PortfolioTransferEntry;
import com.aixohub.portfolio.model.Transaction;

public class CheckTransactionDateAction implements ImportAction {
    @Override
    public Status process(AccountTransaction transaction, Account account) {
        return check(transaction);
    }

    @Override
    public Status process(PortfolioTransaction transaction, Portfolio portfolio) {
        return check(transaction);
    }

    @Override
    public Status process(BuySellEntry entry, Account account, Portfolio portfolio) {
        return check(entry.getAccountTransaction(), entry.getPortfolioTransaction());
    }

    @Override
    public Status process(AccountTransferEntry entry, Account source, Account target) {
        return check(entry.getSourceTransaction(), entry.getTargetTransaction());
    }

    @Override
    public Status process(PortfolioTransferEntry entry, Portfolio source, Portfolio target) {
        return check(entry.getSourceTransaction(), entry.getTargetTransaction());
    }

    private Status check(Transaction... transactions) {
        for (Transaction tx : transactions) {
            if (tx.getDateTime() == null)
                return new Status(Status.Code.ERROR, Messages.IssueTransactionWithoutDate);
        }
        return Status.OK_STATUS;
    }
}
