package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardTransaction {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("seller")
    @Expose
    private String seller;

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

    @SerializedName("buyer")
    @Expose
    private String buyer;
    @SerializedName("card")
    @Expose
    private String card;
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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
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

    @Override
    public String toString() {
        return "CardTransaction{" +
                "id='" + id + '\'' +
                ", seller='" + seller + '\'' +
                ", buyer='" + buyer + '\'' +
                ", card='" + card + '\'' +
                ", boughtFor=" + boughtFor +
                ", date='" + date + '\'' +
                ", satisfied=" + satisfied +
                ", buyerComment='" + buyerComment + '\'' +
                '}';
    }
}
