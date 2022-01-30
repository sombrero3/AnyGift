package com.example.anygift.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    List<User> users=new LinkedList<>();
    List<GiftCard> cards= new LinkedList<>();
    public static final Model instance = new Model();

    private Model() {
        for (int i=0; i<10;i++){
            GiftCard card=new GiftCard(i);
            cards.add(card);
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<GiftCard> getCards() {
        return cards;
    }

    public void setCards(List<GiftCard> cards) {
        this.cards = cards;
    }
}
