package com.example.lebsmart.LostFoundAnnouncements;

public class LFAAdd {

    public String lfaTitle, lfaDate, lfaDescription;
    // date: get current date/time
    // get the building from user's id in check fragment

    public LFAAdd(String lfaTitle, String lfaDate, String lfaDescription) {
        this.lfaTitle = lfaTitle;
        this.lfaDate = lfaDate;
        this.lfaDescription = lfaDescription;
    }

}
