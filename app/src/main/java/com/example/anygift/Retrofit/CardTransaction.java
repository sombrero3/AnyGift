package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardTransaction {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cardType")
    @Expose
    private String cardType;
    @SerializedName("seller")
    @Expose
    private String seller;
    @SerializedName("buyer")
    @Expose
    private String buyer;
    @SerializedName("sellerEmail")
    @Expose
    private String sellerEmail;
    @SerializedName("buyerEmail")
    @Expose
    private String buyerEmail;
    @SerializedName("card")
    @Expose
    private String card;
    @SerializedName("boughtFor")
    @Expose
    private Double boughtFor;
    @SerializedName("cardValue")
    @Expose
    private Double cardValue;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("satisfied")
    @Expose
    private Boolean satisfied;
    @SerializedName("buyerComment")
    @Expose
    private String buyerComment;

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

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Double getCardValue() {
        return cardValue;
    }

    public void setCardValue(Double cardValue) {
        this.cardValue = cardValue;
    }

    @Override
    public String toString() {
        return "CardTransaction{" +
                "id='" + id + '\'' +
                ", cardType='" + cardType + '\'' +
                ", seller='" + seller + '\'' +
                ", buyer='" + buyer + '\'' +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", card='" + card + '\'' +
                ", boughtFor=" + boughtFor +
                ", cardValue=" + cardValue +
                ", date='" + date + '\'' +
                ", satisfied=" + satisfied +
                ", buyerComment='" + buyerComment + '\'' +
                '}';
    }
}
