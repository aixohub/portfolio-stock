package com.aixohub.portfolio.datatransfer.pdf;

import com.aixohub.portfolio.datatransfer.pdf.PDFParser.Block;
import com.aixohub.portfolio.datatransfer.pdf.PDFParser.DocumentType;
import com.aixohub.portfolio.datatransfer.pdf.PDFParser.ParsedData;
import com.aixohub.portfolio.datatransfer.pdf.PDFParser.Transaction;
import com.aixohub.portfolio.model.AccountTransaction;
import com.aixohub.portfolio.model.Client;

import java.util.function.BiConsumer;

import static com.aixohub.portfolio.util.TextUtil.trim;

@SuppressWarnings("nls")
public class SolarisbankAGPDFExtractor extends AbstractPDFExtractor {

    private static final String DEPOSIT = "^(?<date>[\\d]{2}\\.[\\d]{2}\\.[\\d]{4}).*(SEPA\\-.berweisung|.berweisung|Kartenvorgang)(?<note>.*) (?<amount>[\\.,\\d]+) (?<currency>\\w{3})$";
    private static final String REMOVAL = "^(?<date>[\\d]{2}\\.[\\d]{2}\\.[\\d]{4}).*(Kartenzahlung|.berweisung|SEPA-Lastschrift|SEPA\\-.berweisung)(?<note>.*) (?<amount>\\-[\\.,\\d]+) (?<currency>\\w{3})$";

    private static final String CONTEXT_KEY_DATE = "date";
    private static final String CONTEXT_KEY_AMOUNT = "amount";
    private static final String CONTEXT_KEY_CURRENCY = "currency";
    private static final String CONTEXT_KEY_NOTE = "note";

    public SolarisbankAGPDFExtractor(Client client) {
        super(client);

        addBankIdentifier("Solarisbank");

        addAccountStatementTransaction();
    }

    @Override
    public String getLabel() {
        return "Solarisbank AG";
    }

    private void addAccountStatementTransaction() {
        DocumentType type = new DocumentType("Rechnungsabschluss");
        this.addDocumentTyp(type);

        Block depositBlock = new Block(DEPOSIT);
        depositBlock.set(depositTransaction(DEPOSIT));
        type.addBlock(depositBlock);

        Block removalBlock = new Block(REMOVAL);
        removalBlock.set(removalTransaction(REMOVAL));
        type.addBlock(removalBlock);
    }


    private Transaction<AccountTransaction> depositTransaction(String regex) {
        return new Transaction<AccountTransaction>().subject(() -> {
                    AccountTransaction entry = new AccountTransaction();
                    entry.setType(AccountTransaction.Type.DEPOSIT);
                    return entry;
                })
                .section(CONTEXT_KEY_DATE, CONTEXT_KEY_AMOUNT, CONTEXT_KEY_CURRENCY, CONTEXT_KEY_NOTE)
                .match(regex)
                .assign(assignmentsProvider())
                .wrap(TransactionItem::new);
    }

    private Transaction<AccountTransaction> removalTransaction(String regex) {
        return new Transaction<AccountTransaction>().subject(() -> {
                    AccountTransaction entry = new AccountTransaction();
                    entry.setType(AccountTransaction.Type.REMOVAL);
                    return entry;
                })
                .section(CONTEXT_KEY_DATE, CONTEXT_KEY_AMOUNT, CONTEXT_KEY_CURRENCY, CONTEXT_KEY_NOTE)
                .match(regex)
                .assign(assignmentsProvider())
                .wrap(TransactionItem::new);
    }

    private BiConsumer<AccountTransaction, ParsedData> assignmentsProvider() {
        return (transaction, matcherMap) -> {
            transaction.setDateTime(asDate(matcherMap.get(CONTEXT_KEY_DATE)));
            transaction.setAmount(asAmount(matcherMap.get(CONTEXT_KEY_AMOUNT)));
            transaction.setCurrencyCode(matcherMap.get(CONTEXT_KEY_CURRENCY));
            transaction.setNote(trim(matcherMap.get(CONTEXT_KEY_NOTE)));
        };
    }

}
