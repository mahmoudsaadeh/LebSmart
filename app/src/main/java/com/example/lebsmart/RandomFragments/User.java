package com.example.lebsmart.RandomFragments;

public class User {

    String fullName;
    String phone;
    String email;
    String userType;
    String buildingChosen;

    public User(String fullName1, String phone, String email, String userType, String buildingC) {
        this.fullName = fullName1;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullname) {
        this.fullName = fullname;
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
