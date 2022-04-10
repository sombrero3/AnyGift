package com.example.anygift.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User {

    final public static String COLLECTION_NAME = "users";
    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("username")
    @Expose
    String username;
    @SerializedName("firstName")
    @Expose
    String firstName;

    @SerializedName("lastName")
    @Expose
    String lastName;
    @SerializedName("phone")
    @Expose
    String phone;
    @SerializedName("email")
    @Expose

    String email;
    @SerializedName("password")
    @Expose

    String password;
    @SerializedName("isAdmin")
    @Expose

    boolean isAdmin;
    @SerializedName("address")
    @Expose

    String address;

    String latAndLong=null;
    @SerializedName("createdAt")
    @Expose
    String createdAt;
    @SerializedName("lastUpdate")
    @Expose
    String lastUpdate;
    @SerializedName("coins")
    @Expose

    double coins;
    @SerializedName("rating")
    @Expose

    double rating;
    @SerializedName("document")
    @Expose
    String document;
    List<GiftCard> giftCards=null;
    String imageUrl = null;


    public User(){}
    public User(String firstName,String lastName,String phone,String email,String address,String password,String latAndLong){
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.email=email;
        this.address=address;
        this.password=password;
        this.id=email;
        this.latAndLong=latAndLong;
    }
    public User(User other){
        this.firstName = other.firstName;
        this.id = other.id;
        this.lastName = other.lastName;
        this.phone = other.phone;
        this.email= other.email;
        this.password= other.password;
        this.address= other.address;
        this.latAndLong= other.latAndLong;
        this.lastUpdate= other.lastUpdate;
        this.giftCards= other.getGiftCards();
        this.imageUrl= other.imageUrl;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<GiftCard> getGiftCards() {
        return giftCards;
    }

    public void setGiftCards(List<GiftCard> giftCards) {
        this.giftCards = giftCards;
    }

    public String getLastUpdated() {
        return lastUpdate;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdate = lastUpdated;
    }

    public String getLatAndLong() {
        return latAndLong;
    }

    public void setLatAndLong(String latAndLong) {
        this.latAndLong = latAndLong;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("phone", phone);
        result.put("email", email);
        result.put("password", password);
        result.put("address", address);
        result.put("lastUpdated", FieldValue.serverTimestamp().toString());
        result.put("imageUrl", imageUrl);
        result.put("latAndLong", this.latAndLong);
        return result;
    }

    public void fromMap(Map<String, Object> map) {
        this.id = (String) map.get("id");
        this.firstName = (String) map.get("firstName");
        this.lastName = (String) map.get("lastName");
        this.phone= (String) map.get("phone");
        this.email= (String) map.get("email");
        this.password= (String) map.get("password");
        this.latAndLong= (String) map.get("latAndLong");
        this.imageUrl = (String) map.get("imageUrl");
        try {
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(map.get("lastUpdated")));
            Timestamp ts = new Timestamp(date1);
//            this.lastUpdated = ts.getSeconds();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //this.latitude = Double.valueOf(map.get("latitude").toString());
       // this.longitude = Double.valueOf(map.get("longitude").toString());
    }

    public String getName() {
        return this.getFirstName()+ " "+ this.getLastName();
    }
    public Map<String, Object> toMapObject() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("phone", phone);
        result.put("email", email);
        result.put("password", password);
        result.put("address", address);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        result.put("imageUrl", imageUrl);
        result.put("latAndLong", this.latAndLong);
        return result;
    }
    public void fromMapObject(Map<String, Object> map) {
        this.id = (String) map.get("id");
        this.firstName = (String) map.get("firstName");
        this.lastName = (String) map.get("lastName");
        this.phone= (String) map.get("phone");
        this.email= (String) map.get("email");
        this.password= (String) map.get("password");
        this.latAndLong= (String) map.get("latAndLong");
        this.imageUrl = (String) map.get("imageUrl");
        Timestamp ts = (Timestamp) map.get("lastUpdated");
//        this.lastUpdated = ts.getSeconds();
        //this.latitude = Double.valueOf(map.get("latitude").toString());
        // this.longitude = Double.valueOf(map.get("longitude").toString());
    }


}
