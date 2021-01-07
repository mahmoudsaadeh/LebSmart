package com.example.lebsmart.BestServiceProviders;

public class BSP {

    String spName;
    String spPhone;
    String spJob;
    String spRating;
    String spPlaceOfResidence;

    boolean expanded;

    public BSP(String spName, String spPhone, String spJob, String spRating, String spPlaceOfResidence) {
        this.spName = spName;
        this.spPhone = spPhone;
        this.spJob = spJob;
        this.spRating = spRating;
        this.spPlaceOfResidence = spPlaceOfResidence;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getSpPhone() {
        return spPhone;
    }

    public void setSpPhone(String spPhone) {
        this.spPhone = spPhone;
    }

    public String getSpJob() {
        return spJob;
    }

    public void setSpJob(String spJob) {
        this.spJob = spJob;
    }

    public String getSpRating() {
        return spRating;
    }

    public void setSpRating(String spRating) {
        this.spRating = spRating;
    }

    public String getSpPlaceOfResidence() {
        return spPlaceOfResidence;
    }

    public void setSpPlaceOfResidence(String spPlaceOfResidence) {
        this.spPlaceOfResidence = spPlaceOfResidence;
    }
}
