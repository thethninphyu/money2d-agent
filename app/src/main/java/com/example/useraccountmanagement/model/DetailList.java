
package com.example.useraccountmanagement.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailList extends RecyclerView.Adapter {

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

    public DetailList(Context context, ArrayList<DetailList> arrayList) {
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
