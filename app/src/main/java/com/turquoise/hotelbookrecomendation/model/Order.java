package com.turquoise.hotelbookrecomendation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {
    public final static int Furture_order=0;
    public final static int Serving_order=1;
    public final static int Finished_order=2;
    public final static int Cancelled_order=3;
    public final static int Refunded_order=4;
    public final static int To_Refund_order=5;
    public final static int UnRefunded_order=6;
    @SerializedName("orderid")
    @Expose
    private int orderid;
    @SerializedName("houseid")
    @Expose
    private int houseid;
    @SerializedName("userid")
    @Expose
    private int userid;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("totalprice")
    @Expose
    private int totalprice;

    @SerializedName("startdate")
    @Expose
    private String startdate;
    @SerializedName("enddate")
    @Expose
    private String enddate;
    @SerializedName("orderstate")
    @Expose
    private int orderstate;
    @SerializedName("orderpoint")
    @Expose
    private int orderpoint;
    @SerializedName("assessment")
    @Expose
    private String assessment;
    @SerializedName("name")
    @Expose
    private String name;

    @Override
    public String toString() {
        return "Order{" +
                "orderid='" + orderid + '\'' +
                ", houseid='" + houseid + '\'' +
                ", userid='" + userid + '\'' +
                ", duration='" + duration + '\'' +
                ", totalprice='" + totalprice + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", orderstate='" + orderstate + '\'' +
                ", orderpoint='" + orderpoint + '\'' +
                ", assessment='" + assessment + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getHouseid() {
        return houseid;
    }

    public void setHouseid(int houseid) {
        this.houseid = houseid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public int getOrderstate() {
        return orderstate;
    }

    public void setOrderstate(int orderstate) {
        this.orderstate = orderstate;
    }

    public int getOrderpoint() {
        return orderpoint;
    }

    public void setOrderpoint(int orderpoint) {
        this.orderpoint = orderpoint;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
