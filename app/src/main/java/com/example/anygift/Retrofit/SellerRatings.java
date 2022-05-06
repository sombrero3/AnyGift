package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SellerRatings {
    @SerializedName("total")
    @Expose
    private Integer total;

    @SerializedName("good")
    @Expose
    private Integer good;
    @SerializedName("bad")
    @Expose
    private Integer bad;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }


    @Override
    public String toString() {
        return "SellerRatings{" +
                "total=" + total +
                ", good=" + good +
                ", bad=" + bad +
                '}';
    }
}
