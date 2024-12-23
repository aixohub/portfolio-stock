package com.aixohub.portfolio.json;

import com.aixohub.portfolio.model.Account;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Portfolio;
import com.aixohub.portfolio.model.PortfolioTransaction;
import com.aixohub.portfolio.model.TransactionOwner;
import com.aixohub.portfolio.model.TransactionPair;
import com.aixohub.portfolio.money.Values;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JTransaction {
    private Type type;
    private String account;
    private String portfolio;
    private String otherAccount;
    private String otherPortfolio;
    private LocalDate date;
    private LocalTime time;
    private String currency;
    private double amount;
    private String note;
    private Double shares;
    private JSecurity security;
    private List<JTransactionUnit> units;

    @SuppressWarnings("unchecked")
    public static JTransaction from(TransactionPair<?> transaction) {
        JTransaction jtx = new JTransaction();

        TransactionOwner<?> owner = transaction.getOwner();
        if (owner instanceof Account)
            fillFromAccountTransaction((TransactionPair<AccountTransaction>) transaction, jtx);
        else if (owner instanceof Portfolio)
            fillFromPortfolioTransaction((TransactionPair<PortfolioTransaction>) transaction, jtx);

        jtx.setDate(transaction.getTransaction().getDateTime().toLocalDate());
        LocalTime time = transaction.getTransaction().getDateTime().toLocalTime();
        if (!time.equals(LocalTime.MIDNIGHT))
            jtx.setTime(time);

        jtx.setCurrency(transaction.getTransaction().getCurrencyCode());
        jtx.setAmount(transaction.getTransaction().getAmount() / Values.Amount.divider());

        jtx.setNote(transaction.getTransaction().getNote());

        if (transaction.getTransaction().getSecurity() != null)
            jtx.setSecurity(JSecurity.from(transaction.getTransaction().getSecurity()));

        if (transaction.getTransaction().getShares() != 0)
            jtx.shares = transaction.getTransaction().getShares() / Values.Share.divider();

        transaction.getTransaction().getUnits().map(JTransactionUnit::from).forEach(jtx::addUnit);

        return jtx;
    }

    private static void fillFromAccountTransaction(TransactionPair<AccountTransaction> tx, JTransaction jtx) {
        jtx.account = tx.getOwner().toString();

        AccountTransaction.Type tType = tx.getTransaction().getType();
        switch (tType) {
            case DEPOSIT:
                jtx.type = Type.DEPOSIT;
                break;
            case REMOVAL:
                jtx.type = Type.REMOVAL;
                break;
            case TRANSFER_OUT:
                jtx.type = Type.CASH_TRANSFER;
                jtx.otherAccount = tx.getTransaction().getCrossEntry()
                        .getCrossOwner(tx.getTransaction()).toString();
                break;
            case TRANSFER_IN:
                jtx.type = Type.CASH_TRANSFER;
                jtx.otherAccount = jtx.account;
                jtx.account = tx.getTransaction().getCrossEntry().getCrossOwner(tx.getTransaction())
                        .toString();
                break;
            case DIVIDENDS:
                jtx.type = Type.DIVIDEND;
                break;
            case INTEREST:
                jtx.type = Type.INTEREST;
                break;
            case INTEREST_CHARGE:
                jtx.type = Type.INTEREST_CHARGE;
                break;
            case TAXES:
                jtx.type = Type.TAX;
                break;
            case TAX_REFUND:
                jtx.type = Type.TAX_REFUND;
                break;
            case FEES:
                jtx.type = Type.FEE;
                break;
            case FEES_REFUND:
                jtx.type = Type.FEE_REFUND;
                break;
            case BUY:
            case SELL:
                fillFromPortfolioTransaction(
                        new TransactionPair<PortfolioTransaction>(
                                (Portfolio) tx.getTransaction().getCrossEntry()
                                        .getCrossOwner(tx.getTransaction()),
                                (PortfolioTransaction) tx.getTransaction().getCrossEntry()
                                        .getCrossTransaction(tx.getTransaction())),
                        jtx);
                break;
            default:
                throw new IllegalArgumentException(
                        "unsupported transaction type '" + tType + "' for transaction " + jtx.toJson()); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static void fillFromPortfolioTransaction(TransactionPair<PortfolioTransaction> transaction,
                                                     JTransaction jtx) {
        jtx.portfolio = transaction.getOwner().toString();

        PortfolioTransaction.Type type = transaction.getTransaction().getType();

        switch (type) {
            case BUY:
                jtx.type = Type.PURCHASE;
                jtx.account = transaction.getTransaction().getCrossEntry().getCrossOwner(transaction.getTransaction())
                        .toString();
                break;
            case SELL:
                jtx.type = Type.SALE;
                jtx.account = transaction.getTransaction().getCrossEntry().getCrossOwner(transaction.getTransaction())
                        .toString();
                break;
            case TRANSFER_OUT:
                jtx.type = Type.SECURITY_TRANSFER;
                jtx.otherPortfolio = transaction.getTransaction().getCrossEntry()
                        .getCrossOwner(transaction.getTransaction()).toString();
                break;
            case TRANSFER_IN:
                jtx.type = Type.SECURITY_TRANSFER;
                jtx.otherPortfolio = jtx.portfolio;
                jtx.portfolio = transaction.getTransaction().getCrossEntry().getCrossOwner(transaction.getTransaction())
                        .toString();
                break;
            case DELIVERY_INBOUND:
                jtx.type = Type.INBOUND_DELIVERY;
                break;
            case DELIVERY_OUTBOUND:
                jtx.type = Type.OUTBOUND_DELIVERY;
                break;
            default:
                throw new IllegalArgumentException(
                        "unsupported transaction type '" + type + "' for transaction " + transaction); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getOtherAccount() {
        return otherAccount;
    }

    public void setOtherAccount(String otherAccount) {
        this.otherAccount = otherAccount;
    }

    public String getOtherPortfolio() {
        return otherPortfolio;
    }

    public void setOtherPortfolio(String otherPortfolio) {
        this.otherPortfolio = otherPortfolio;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public JSecurity getSecurity() {
        return security;
    }

    public void setSecurity(JSecurity security) {
        this.security = security;
    }

    public Stream<JTransactionUnit> getUnits() {
        return units == null ? Stream.empty() : units.stream();
    }

    public void addUnit(JTransactionUnit unit) {
        if (units == null)
            units = new ArrayList<>();

        units.add(unit);
    }

    public String toJson() {
        return JClient.GSON.toJson(this);
    }

    public enum Type {
        PURCHASE, SALE, INBOUND_DELIVERY, OUTBOUND_DELIVERY, SECURITY_TRANSFER, CASH_TRANSFER, DEPOSIT, REMOVAL, DIVIDEND, INTEREST, INTEREST_CHARGE, TAX, TAX_REFUND, FEE, FEE_REFUND
    }
}
