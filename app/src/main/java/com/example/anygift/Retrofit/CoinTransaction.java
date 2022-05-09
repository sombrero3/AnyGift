package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class CoinTransaction {
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private Double to;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("date")
    @Expose
    private String date;

    public CoinTransaction(){}
    public CoinTransaction(String from, Double to, Double amount, String date) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Double getTo() {
        return to;
    }

    public void setTo(Double to) {
        this.to = to;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CoinTransaction{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }

    public static HashMap<String,Object> mapToAddCoinTransaction(String from,String to, Double amount){
        return new HashMap<String, Object>() {{
            put("from", from);
            put("to", to);
            put("amount", amount);
        }};
    }
}
