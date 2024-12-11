package com.aixohub.portfolio.datatransfer.actions;

import com.aixohub.portfolio.Messages;
import com.aixohub.portfolio.datatransfer.ImportAction;
import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransaction.Type;
import com.aixohub.portfolio.money.Values;

import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Set;

public class CheckSecurityRelatedValuesAction implements ImportAction {
    @Override
    public Status process(AccountTransaction transaction, Account account) {
        boolean hasSecurity = transaction.getSecurity() != null;

        Set<Type> typesWithOptionalSecurity = EnumSet.of(Type.DIVIDENDS, Type.TAXES, Type.TAX_REFUND, Type.FEES,
                Type.FEES_REFUND);

        if (hasSecurity && !typesWithOptionalSecurity.contains(transaction.getType()))
            return new Status(Status.Code.ERROR,
                    MessageFormat.format(Messages.MsgCheckTransactionTypeCannotHaveASecurity,
                            transaction.getType(), transaction.getSecurity().getName()));

        if (!hasSecurity && transaction.getType() == Type.DIVIDENDS)
            return new Status(Status.Code.ERROR, Messages.MsgCheckDividendsMustHaveASecurity);

        if (transaction.getShares() != 0
                && (!hasSecurity || !typesWithOptionalSecurity.contains(transaction.getType())))
            return new Status(Status.Code.ERROR, MessageFormat.format(Messages.MsgCheckTransactionTypeCannotHaveShares,
                    transaction.getType(), Values.Share.format(transaction.getShares())));

        return Status.OK_STATUS;
    }
}
