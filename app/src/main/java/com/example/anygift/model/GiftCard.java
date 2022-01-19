package com.example.anygift.model;

import java.util.Date;

public class GiftCard {
    int id;
    String cardName;
    double value;
    Date expirationDate;
    enum type{
        GENERAL,
        CLOTHING,
        VACATIONS,
        SPORTS
    }
    double wantedPrice;
    User owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getWantedPrice() {
        return wantedPrice;
    }

    public void setWantedPrice(double wantedPrice) {
        this.wantedPrice = wantedPrice;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
