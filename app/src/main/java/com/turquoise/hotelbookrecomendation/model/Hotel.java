package com.turquoise.hotelbookrecomendation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hotel implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ratings")
    @Expose
    private String ratings;
    @SerializedName("visits")
    @Expose
    private String visits;
    @SerializedName("completedBookings")
    @Expose
    private String completedBookings;
    @SerializedName("draftBookings")
    @Expose
    private String draftBookings;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl="";
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("price")
    @Expose
    private int price;

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", ratings='" + ratings + '\'' +
                ", visits='" + visits + '\'' +
                ", completedBookings='" + completedBookings + '\'' +
                ", draftBookings='" + draftBookings + '\'' +
                ", tags='" + tags + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", id='" + id + '\'' +
                ", price=" + price +
                ", holderid='" + holderid + '\'' +
                ", address='" + address + '\'' +
                ", housestate='" + housestate + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHolderid() {
        return holderid;
    }

    public void setHolderid(String holderid) {
        this.holderid = holderid;
    }

    public String getHousestate() {
        return housestate;
    }

    @SerializedName("holderid")
    @Expose
    private String holderid;
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("housestate")
    @Expose
    private String housestate;
    @SerializedName("detail")
    @Expose
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        detail = detail;
    }




    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }





    public String getHousestatetate() {
        return housestate;
    }

    public void setHousestate(String state) {
        this.housestate = state;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getVisits() {
        return visits;
    }

    public void setVisits(String visits) {
        this.visits = visits;
    }

    public String getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(String completedBookings) {
        this.completedBookings = completedBookings;
    }

    public String getDraftBookings() {
        return draftBookings;
    }

    public void setDraftBookings(String draftBookings) {
        this.draftBookings = draftBookings;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}