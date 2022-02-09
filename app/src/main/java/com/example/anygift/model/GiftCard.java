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

}
