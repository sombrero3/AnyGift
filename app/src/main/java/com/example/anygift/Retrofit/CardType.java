package com.example.anygift.Retrofit;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardType {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("categories")
    @Expose
    private List<String> categories = null;

    @SerializedName("stores")
    @Expose
    private List<Store> stores = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("picture")
    @Expose
    private Bitmap picture;

    public CardType(String name, List<String> categories, List<Store> stores,String id) {
        this.name = name;
        this.categories = categories;
        this.stores = stores;
        this.id = id;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "CardType{" +
                "name='" + name + '\'' +
                ", categories=" + categories +
                ", stores=" + stores +
                ", id='" + id + '\'' +
                ", picture=" + picture +
                '}';
    }


}