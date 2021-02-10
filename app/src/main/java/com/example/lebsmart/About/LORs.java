package com.example.lebsmart.About;

public class LORs {

    String fullNames, mails, phones, userTypes;

    boolean isExpanded;

    public LORs(String fullNames, String mails, String phones, String userTypes) {
        this.fullNames = fullNames;
        this.mails = mails;
        this.phones = phones;
        this.userTypes = userTypes;

        this.isExpanded = false;
    }


    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getMails() {
        return mails;
    }

    public void setMails(String mails) {
        this.mails = mails;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(String userTypes) {
        this.userTypes = userTypes;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
