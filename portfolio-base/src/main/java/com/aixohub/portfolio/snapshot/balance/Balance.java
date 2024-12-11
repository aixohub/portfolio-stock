package com.aixohub.portfolio.snapshot.balance;

import com.aixohub.portfolio.model.Transaction;
import com.aixohub.portfolio.money.Money;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Balance {
    private final LocalDate date;
    private final Money value;
    private final List<Proposal> proposals = new ArrayList<>();
    public Balance(LocalDate date, Money value) {
        this.date = date;
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public Money getValue() {
        return value;
    }

    public void addProposal(Proposal proposal) {
        this.proposals.add(proposal);
    }

    public List<Proposal> getProposals() {
        return this.proposals;
    }

    public static class Proposal {
        private final String category;
        private final List<Transaction> candidates;

        public Proposal(String category, List<Transaction> candidates) {
            this.category = category;
            this.candidates = candidates;
        }

        public String getCategory() {
            return category;
        }

        public List<Transaction> getCandidates() {
            return candidates;
        }
    }
}
