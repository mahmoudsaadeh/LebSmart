package com.example.lebsmart.CommitteeDR;

public class CommitteeDR {

    String announcementTitle;
    String announcementDescription;
    String announcementType; // decision or reminder, use radio group
    String announcementDate; // get current date
    String addedBy;

    //String voteChoice; // with or against

    boolean expanded;

    public CommitteeDR(String announcementTitle, String announcementDescription, String announcementType,
                       String announcementDate, String addedBy) {
        this.announcementTitle = announcementTitle;
        this.announcementDescription = announcementDescription;
        this.announcementType = announcementType;
        this.announcementDate = announcementDate;
        this.addedBy = addedBy;
        //this.voteChoice = voteChoice;
        this.expanded = false;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementDescription() {
        return announcementDescription;
    }

    public void setAnnouncementDescription(String announcementDescription) {
        this.announcementDescription = announcementDescription;
    }

    public String getAnnouncementType() {
        return announcementType;
    }

    public void setAnnouncementType(String announcementType) {
        this.announcementType = announcementType;
    }

    public String getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(String announcementDate) {
        this.announcementDate = announcementDate;
    }

}
