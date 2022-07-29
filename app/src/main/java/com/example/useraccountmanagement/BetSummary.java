package com.example.useraccountmanagement;

public class BetSummary {
    int points;
    int user;
    String number;

    public BetSummary() {
    }

    public BetSummary(int points, int user, String number) {
        this.points = points;
        this.user = user;
        this.number = number;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
