package com.example.anygift.model;

import java.util.Date;

public class GiftCard {
    int id;
    String cardName;
    double value;
    Date expirationDate;
    boolean flag;
    enum type{
        GENERAL,
        CLOTHING,
        VACATIONS,
        SPORTS
    }
    double wantedPrice;
    User owner;

    public GiftCard(int id){
        this.id=id;

        cardName="Card #"+Integer.toString(id);


    }

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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

