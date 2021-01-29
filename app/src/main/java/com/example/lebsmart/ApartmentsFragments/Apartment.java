package com.example.lebsmart.ApartmentsFragments;

public class Apartment {

    // add other data (attributes) that belong to an apartment as needed
    String state; //rent - sale
    String price;
    String area;
    String building;
    String phoneNumber;
    //String floor; // take from user after he chooses a building, display a drop-down that shows the available
                    //number of floors within the building only
    String ownerName;

    boolean expanded;

    public Apartment(String state, String price, String area, String building, String phoneNumber, String ownerName1) {
        this.state = state;
        this.price = price;
        this.area = area;
        this.building = building;
        this.phoneNumber = phoneNumber;
        //this.floor = floor;
        this.ownerName = ownerName1;

        this.expanded = false;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /*public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }*/

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
