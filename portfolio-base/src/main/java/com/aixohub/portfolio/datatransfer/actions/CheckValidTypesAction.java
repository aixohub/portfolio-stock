package com.aixohub.portfolio.datatransfer.actions;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.datatransfer.ImportAction;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;

import java.text.MessageFormat;

public class CheckValidTypesAction implements ImportAction {
    @Override
    public Status process(AccountTransaction transaction, Account account) {
        switch (transaction.getType()) {
            case BUY:
            case SELL:
            case TRANSFER_IN:
            case TRANSFER_OUT:
                return new Status(Status.Code.ERROR, MessageFormat.format(Messages.MsgCheckInvalidTransactionType,
                        transaction.getType().toString()));
            case DEPOSIT:
            case DIVIDENDS:
            case INTEREST:
            case INTEREST_CHARGE:
            case TAX_REFUND:
            case TAXES:
            case REMOVAL:
            case FEES:
            case FEES_REFUND:
                return Status.OK_STATUS;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public Status process(PortfolioTransaction transaction, Portfolio portfolio) {
        switch (transaction.getType()) {
            case BUY:
            case SELL:
            case TRANSFER_IN:
            case TRANSFER_OUT:
                return new Status(Status.Code.ERROR, MessageFormat.format(Messages.MsgCheckInvalidTransactionType,
                        transaction.getType().toString()));
            case DELIVERY_INBOUND:
            case DELIVERY_OUTBOUND:
                return Status.OK_STATUS;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
