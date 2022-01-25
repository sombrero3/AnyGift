package com.example.anygift.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class GiftCard {
    @PrimaryKey
    @NonNull
    String id="";
    String cardName;
    double value;
    Long expirationDate;
    double wantedPrice;
    //User owner;
    public enum type{
        GENERAL,
        CLOTHING,
        VACATIONS,
        SPORTS
    }

    public GiftCard(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getWantedPrice() {
        return wantedPrice;
    }

    public void setWantedPrice(double wantedPrice) {
        this.wantedPrice = wantedPrice;
    }

}
