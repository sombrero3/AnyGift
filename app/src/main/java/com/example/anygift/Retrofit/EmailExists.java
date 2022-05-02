package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmailExists {
    public Boolean getEmail_exists() {
        return email_exists;
    }

    public void setEmail_exists(Boolean email_exists) {
        this.email_exists = email_exists;
    }

    @SerializedName("email_exists")
    @Expose
    private Boolean email_exists;

}
