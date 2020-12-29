package com.example.lebsmart;

public class Meetings {

    String meetingTime;
    String meetingDate;
    String meetingPlace;
    String scheduledBy;
    String meetingTitle;
    String meetingDescription;

    boolean expanded;

    public Meetings(String meetingTime, String meetingDate, String meetingPlace, String scheduledBy, String meetingTitle, String meetingDescription) {
        this.meetingTime = meetingTime;
        this.meetingDate = meetingDate;
        this.meetingPlace = meetingPlace;
        this.scheduledBy = scheduledBy;
        this.meetingTitle = meetingTitle;
        this.meetingDescription = meetingDescription;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public void setMeetingDescription(String meetingDescription) {
        this.meetingDescription = meetingDescription;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public String getScheduledBy() {
        return scheduledBy;
    }

    public void setScheduledBy(String scheduledBy) {
        this.scheduledBy = scheduledBy;
    }

}
