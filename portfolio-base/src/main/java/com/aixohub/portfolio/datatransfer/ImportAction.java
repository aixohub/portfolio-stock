package com.aixohub.portfolio.datatransfer;

import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.AccountTransferEntry;
import com.aixohub.portfolio.model.BuySellEntry;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.PortfolioTransferEntry;
import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.model.SecurityPrice;

public interface ImportAction {
    default Status process(Security security) {
        return Status.OK_STATUS;
    }

    default Status process(Security security, SecurityPrice price) {
        return Status.OK_STATUS;
    }

    default Status process(AccountTransaction transaction, Account account) {
        return Status.OK_STATUS;
    }

    default Status process(PortfolioTransaction transaction, Portfolio portfolio) {
        return Status.OK_STATUS;
    }

    default Status process(BuySellEntry entry, Account account, Portfolio portfolio) {
        return Status.OK_STATUS;
    }

    default Status process(AccountTransferEntry entry, Account source, Account target) {
        return Status.OK_STATUS;
    }

    default Status process(PortfolioTransferEntry entry, Portfolio source, Portfolio target) {
        return Status.OK_STATUS;
    }

    interface Context {
        Account getAccount();

        Portfolio getPortfolio();

        Account getSecondaryAccount();

        Portfolio getSecondaryPortfolio();
    }

    class Status {
        public static final Status OK_STATUS = new Status(Code.OK, null);
        private final Code code;
        private final String message;
        public Status(Code code, String message) {
            this.code = code;
            this.message = message;
        }

        public Code getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public enum Code {
            OK, WARNING, ERROR;

            public boolean isHigherSeverityAs(Code other) {
                return ordinal() > other.ordinal();
            }
        }
    }
}
