package com.aixohub.portfolio.json;

import com.aixohub.portfolio.json.impl.LocalDateAdapter;
import com.aixohub.portfolio.json.impl.LocalTimeAdapter;
import com.aixohub.portfolio.model.TransactionPair;
import com.google.common.io.Files;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JClient {
    public static final Gson GSON = new GsonBuilder() //
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter().nullSafe())
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

    private int version = 1;

    private List<JTransaction> transactions;

    public static JClient from(List<TransactionPair<?>> transactions) {
        JClient client = new JClient();
        transactions.stream().map(JTransaction::from).forEach(client::addTransaction);
        return client;
    }

    public static JClient from(String file) throws IOException {
        return GSON.fromJson(Files.asCharSource(new File(file), StandardCharsets.UTF_8).read(), JClient.class);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Stream<JTransaction> getTransactions() {
        return transactions == null ? Stream.empty() : transactions.stream();
    }

    public void addTransaction(JTransaction transaction) {
        if (transactions == null)
            transactions = new ArrayList<>();

        transactions.add(transaction);
    }

    public String toJson() {
        return GSON.toJson(this);
    }
}
