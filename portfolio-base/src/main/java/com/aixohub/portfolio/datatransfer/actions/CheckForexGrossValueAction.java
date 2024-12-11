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
import com.aixohub.portfolio.model.Transaction.Unit;
import com.aixohub.portfolio.money.Money;
import com.aixohub.portfolio.money.Values;

import java.text.MessageFormat;
import java.util.Optional;

public class CheckForexGrossValueAction implements ImportAction {
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
        return check(entry.getPortfolioTransaction(), entry.getAccountTransaction());
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
        for (Transaction t : transactions) {
            Status status = check(t);
            if (status.getCode() != Status.Code.OK)
                return status;
        }
        return Status.OK_STATUS;
    }

    private Status check(Transaction transaction) {
        Optional<Unit> grossValueUnit = transaction.getUnit(Unit.Type.GROSS_VALUE);

        if (grossValueUnit.isEmpty())
            return Status.OK_STATUS;

        Money unitValue = grossValueUnit.get().getAmount();
        Money calculatedValue = transaction.getGrossValue();

        if (!unitValue.equals(calculatedValue))
            return new Status(Status.Code.ERROR,
                    MessageFormat.format(Messages.MsgCheckConfiguredAndCalculatedGrossValueDoNotMatch,
                            Values.Money.format(unitValue), Values.Money.format(calculatedValue)));

        return Status.OK_STATUS;

    }

}
