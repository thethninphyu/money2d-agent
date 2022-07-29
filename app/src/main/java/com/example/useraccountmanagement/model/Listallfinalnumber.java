
package com.example.useraccountmanagement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Listallfinalnumber {

    @SerializedName("number")
    @Expose
    private String number;
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
