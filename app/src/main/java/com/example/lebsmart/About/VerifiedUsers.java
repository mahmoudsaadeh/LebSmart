package com.example.lebsmart.About;

public class VerifiedUsers {

    public String userId, state; // verified or not

    public VerifiedUsers(String userId, String state) {
        this.userId = userId;
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
