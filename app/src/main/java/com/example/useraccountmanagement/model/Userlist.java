
package com.example.useraccountmanagement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Userlist implements Serializable
{

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("user")
    @Expose

    private List<User> user = null;
    private final static long serialVersionUID = -4063183897918258882L;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

}
