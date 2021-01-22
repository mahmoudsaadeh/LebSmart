package com.example.lebsmart.RandomFragments;

public class User {

    String username;
    String phone;
    String email;
    String userType;
    String buildingChosen;

    public User(String username, String phone, String email, String userType, String buildingC) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.userType = userType;
        this.buildingChosen = buildingC;
    }

    public String getBuildingChosen() {
        return buildingChosen;
    }

    public void setBuildingChosen(String building) {
        this.buildingChosen = building;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
