
package com.example.useraccountmanagement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AgentUserLists {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("user")
    @Expose
    private List<AgentUser> user = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<AgentUser> getUser() {
        return user;
    }

    public void setUser(List<AgentUser> user) {
        this.user = user;
    }

}
