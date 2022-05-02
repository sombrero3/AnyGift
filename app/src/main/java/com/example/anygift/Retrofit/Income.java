package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Income {
    @SerializedName("income")
    @Expose
    private Double income;
    @SerializedName("Transactions")
    @Expose
    private Integer transactions;
    public Income(){}
    public Income(Double income, Integer transactions) {
        this.income = income;
        this.transactions = transactions;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Integer getTransactions() {
        return transactions;
    }

    public void setTransactions(Integer transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Income{" +
                "income=" + income +
                ", transactions=" + transactions +
                '}';
    }
}