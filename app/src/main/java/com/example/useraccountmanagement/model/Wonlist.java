
package com.example.useraccountmanagement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wonlist {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("point")
    @Expose
    private long point;
    @SerializedName("won")
    @Expose
    private long won;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("agent")
    @Expose
    private String agent;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("date")
    @Expose
    private String date;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getWon() {
        return won;
    }

    public void setWon(long won) {
        this.won = won;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
