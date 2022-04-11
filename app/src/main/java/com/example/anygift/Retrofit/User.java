package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class User {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("isAdmin")
    @Expose
    private Boolean isAdmin;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("lastUpdate")
    @Expose
    private String lastUpdate;
    @SerializedName("coins")
    @Expose
    private Integer coins;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("verfied")
    @Expose
    private Boolean verified;
    @SerializedName("latAndLong")
    @Expose
    private String latAndLong;

    public User() {
    }

    public User(String id, String password, String email, String firstName, String lastName, String phone, String address, Boolean isAdmin, String createdAt, String lastUpdate, Integer coins, Integer rating, String document, Boolean verified, String latAndLong) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.lastUpdate = lastUpdate;
        this.coins = coins;
        this.rating = rating;
        this.document = document;
        this.verified = verified;
        this.latAndLong = latAndLong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getLatAndLong() {
        return latAndLong;
    }

    public void setLatAndLong(String latAndLong) {
        this.latAndLong = latAndLong;
    }


    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", latAndLong='" + latAndLong + '\'' +
                ", isAdmin=" + isAdmin +
                ", createdAt='" + createdAt + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", coins=" + coins +
                ", rating=" + rating +
                ", document='" + document + '\'' +
                ", verified=" + verified +
                '}';
    }

    public static HashMap<String, Object> mapToAddUser(String firstName, String lastName, String email,
                                                       String password, String address, String latAndLong, String phone, boolean isAdmin,
                                                       String profilePicture) {
        return new HashMap<String, Object>() {{
            put("firstName", firstName);
            put("lastName", lastName);
            put("email", email);
            put("password", password);
            put("address", address);
            put("latAndLong", latAndLong);
            put("phone", phone);
            put("isAdmin", isAdmin);
            put("profilePicture", profilePicture);
        }};
    }
}