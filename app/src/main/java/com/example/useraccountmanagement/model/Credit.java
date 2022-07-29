
package com.example.useraccountmanagement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Credit {

    @SerializedName("amount")
    @Expose
    public Integer amount;
    @SerializedName("paid")
    @Expose
    public Boolean paid;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("date")
    @Expose
    public String date;


    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
