package com.turquoise.hotelbookrecomendation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderResult implements Serializable {

    @SerializedName("results")
    @Expose
    private List< Order>  Orders = null;

    public List< Order> getOrders() {
        return  Orders;
    }

    public void setOrders(List<Order>  Orders) {
        this. Orders =  Orders;
    }

}