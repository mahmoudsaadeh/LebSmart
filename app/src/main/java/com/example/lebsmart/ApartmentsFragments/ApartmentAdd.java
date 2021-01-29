package com.example.lebsmart.ApartmentsFragments;

public class ApartmentAdd {

    // class used to pass an object to save data on Firebase

    public String state; //rent - sale
    public String price;
    public String area;
    //public String buildingApartment;

    //public String addedBy; // user ID

    public ApartmentAdd(String state, String price, String area) {
        this.state = state;
        this.price = price;
        this.area = area;
        //this.buildingApartment = buildingApartment1;
        //this.addedBy = addedBy1;
    }



}
