package com.example.lebsmart.LostFoundAnnouncements;

public class LFA {

    String titleLFA;
    String dateFoundLFA;
    String descriptionLFA;
    String foundByLFA;
    String foundersBuilding;

    boolean expanded;

    public LFA(String titleLFA, String dateFoundLFA, String descriptionLFA, String foundByLFA, String foundersBuilding) {
        this.titleLFA = titleLFA;
        this.dateFoundLFA = dateFoundLFA;
        this.descriptionLFA = descriptionLFA;
        this.foundByLFA = foundByLFA;
        this.foundersBuilding = foundersBuilding;

        this.expanded = false;
    }

    public String getFoundByLFA() {
        return foundByLFA;
    }

    public void setFoundByLFA(String foundByLFA) {
        this.foundByLFA = foundByLFA;
    }

    public String getFoundersBuilding() {
        return foundersBuilding;
    }

    public void setFoundersBuilding(String foundersBuilding) {
        this.foundersBuilding = foundersBuilding;
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

    public String getfoundByLFA() {
        return foundByLFA;
    }

    public void setfoundByLFA(String foundByLFA) {
        this.foundByLFA = foundByLFA;
    }
}
