package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Outcome {
    @SerializedName("outcome")
    @Expose
    private Double outcome;
    @SerializedName("Transactions")
    @Expose
    private Integer transactions;
    public Outcome(){}
    public Outcome(Double outcome, Integer transactions) {
        this.outcome = outcome;
        this.transactions = transactions;
    }

    public Double getoutcome() {
        return outcome;
    }

    public void setoutcome(Double outcome) {
        this.outcome = outcome;
    }

    public Integer getTransactions() {
        return transactions;
    }

    public void setTransactions(Integer transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "outcome{" +
                "outcome=" + outcome +
                ", transactions=" + transactions +
                '}';
    }
}
