package com.example.lebsmart.LostFoundAnnouncements;

public class LFA {

    String titleLFA;
    String dateFoundLFA;
    String descriptionLFA;
    String addedByLFA; // get current user ID

    boolean expanded;

    public LFA(String titleLFA, String dateFoundLFA, String descriptionLFA, String addedByLFA) {
        this.titleLFA = titleLFA;
        this.dateFoundLFA = dateFoundLFA;
        this.descriptionLFA = descriptionLFA;
        this.addedByLFA = addedByLFA;
        this.expanded = false;
    }


    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    public String getTitleLFA() {
        return titleLFA;
    }

    public void setTitleLFA(String titleLFA) {
        this.titleLFA = titleLFA;
    }

    public String getDateFoundLFA() {
        return dateFoundLFA;
    }

    public void setDateFoundLFA(String dateFoundLFA) {
        this.dateFoundLFA = dateFoundLFA;
    }

    public String getDescriptionLFA() {
        return descriptionLFA;
    }

    public void setDescriptionLFA(String descriptionLFA) {
        this.descriptionLFA = descriptionLFA;
    }

    public String getAddedByLFA() {
        return addedByLFA;
    }

    public void setAddedByLFA(String addedByLFA) {
        this.addedByLFA = addedByLFA;
    }
}
