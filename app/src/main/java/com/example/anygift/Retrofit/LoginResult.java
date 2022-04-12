package com.example.anygift.Retrofit;

import android.text.BoringLayout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class LoginResult {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("isAdmin")
    @Expose
    private Boolean isAdmin;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("latAndLong")
    @Expose
    private String latAndLong;
    @SerializedName("coins")
    @Expose
    private Double coins;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("verified")
    @Expose
    private Boolean verified;

    public LoginResult() {
    }

    public LoginResult(String email, String token, String id, Boolean isAdmin, String lastName, String firstName, String address, String phone, String latAndLong, Double coins, Double rating, String profilePicture, String document, Boolean verified) {
        this.email = email;
        this.token = token;
        this.id = id;
        this.isAdmin = isAdmin;
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.phone = phone;
        this.latAndLong = latAndLong;
        this.coins = coins;
        this.rating = rating;
        this.profilePicture = profilePicture;
        this.document = document;
        this.verified = verified;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public static HashMap<String, String> mapToLogin(String email, String password) {
        return new HashMap<String, String>() {{
            put("email", email);
            put("password", password);
        }};
    }

    public static HashMap<String, Object> mapToAddUser(String firstName, String lastName, String email,
                                                       String password, String address, String latAndLong, String phone, boolean isAdmin) {
        return new HashMap<String, Object>() {{
            put("firstName", firstName);
            put("lastName", lastName);
            put("email", email);
            put("password", password);
            put("address", address);
            put("latAndLong", latAndLong);
            put("phone", phone);
            put("isAdmin", isAdmin);
        }};
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getLatAndLong() {
        return latAndLong;
    }

    public void setLatAndLong(String latAndLong) {
        this.latAndLong = latAndLong;
    }

    public Double getCoins() {
        return coins;
    }

    public void setCoins(Double coins) {
        this.coins = coins;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", id='" + id + '\'' +
                ", isAdmin=" + isAdmin +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", latAndLong='" + latAndLong + '\'' +
                ", coins=" + coins +
                ", rating=" + rating +
                ", profilePicture='" + profilePicture + '\'' +
                ", document='" + document + '\'' +
                ", verified=" + verified +
                '}';
    }
}