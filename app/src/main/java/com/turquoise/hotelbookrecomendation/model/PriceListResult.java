package com.turquoise.hotelbookrecomendation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PriceListResult implements Serializable {

    @SerializedName("results")
    @Expose
    private List< PriceList>  PriceLists = null;

    public List< PriceList> getPriceLists() {
        return  PriceLists;
    }

    public void setPriceLists(List<PriceList>  PriceLists) {
        this. PriceLists =  PriceLists;
    }

}