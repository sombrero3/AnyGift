package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardTransaction {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("from")
    @Expose
    private String from;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(Boolean satisfied) {
        this.satisfied = satisfied;
    }

    public String getBuyerComment() {
        return buyerComment;
    }

    public void setBuyerComment(String buyerComment) {
        this.buyerComment = buyerComment;
    }

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
    @SerializedName("satisfied")
    @Expose
    private Boolean satisfied;
    @SerializedName("buyerComment")
    @Expose
    private String buyerComment;

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
