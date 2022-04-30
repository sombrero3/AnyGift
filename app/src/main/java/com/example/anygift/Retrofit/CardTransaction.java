package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardTransaction {
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private Double to;
    @SerializedName("card")
    @Expose
    private Double card;
    @SerializedName("boughtFor")
    @Expose
    private Double boughtFor;
    @SerializedName("date")
    @Expose
    private String date;


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

    public Double getCard() {
        return card;
    }

    public void setCard(Double card) {
        this.card = card;
    }

    public Double getBoughtFor() {
        return boughtFor;
    }

    public void setBoughtFor(Double boughtFor) {
        this.boughtFor = boughtFor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
