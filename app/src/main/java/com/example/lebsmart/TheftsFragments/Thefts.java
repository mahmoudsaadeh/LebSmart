package com.example.lebsmart.TheftsFragments;

public class Thefts {

    String reportedBy;
    String title;
    String date;
    String time;
    String message;
    String location;

    boolean expanded;

    public Thefts(String reportedBy, String title, String date, String time, String message, String location) {
        this.reportedBy = reportedBy;
        this.title = title;
        this.date = date;
        this.time = time;
        this.message = message;
        this.location = location;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
