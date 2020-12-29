package com.example.lebsmart.ApartmentsFragments;

public class ApartmentAdd {

    // class used to pass an object to save data on Firebase

    String state; //rent - sale
    String price;
    String area;

    public ApartmentAdd(String state, String price, String area) {
        this.state = state;
        this.price = price;
        this.area = area;
    }



}
