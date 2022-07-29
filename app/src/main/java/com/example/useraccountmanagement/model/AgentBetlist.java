
package com.example.useraccountmanagement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgentBetlist {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("user")
    @Expose
    private String user;
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
