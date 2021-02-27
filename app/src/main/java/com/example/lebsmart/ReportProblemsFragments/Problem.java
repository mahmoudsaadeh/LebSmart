package com.example.lebsmart.ReportProblemsFragments;

public class Problem {

    String problemType;//category
    String problemDescription;
    String problemReportedBy; // get current user
    String problemReportDate; // get current date
    String reportersBuilding;

    boolean expanded;

    public Problem(String problemType, String problemDescription, String problemReportedBy
            , String problemReportDate, String reportersBuilding) {
        this.problemType = problemType;
        this.problemDescription = problemDescription;
        this.problemReportedBy = problemReportedBy;
        this.problemReportDate = problemReportDate;

        this.reportersBuilding = reportersBuilding;

        this.expanded = false;
    }

    public String getReportersBuilding() {
        return reportersBuilding;
    }

    public void setReportersBuilding(String reportersBuilding) {
        this.reportersBuilding = reportersBuilding;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expandedProblem) {
        this.expanded = expandedProblem;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getProblemReportedBy() {
        return problemReportedBy;
    }

    public void setProblemReportedBy(String problemReportedBy) {
        this.problemReportedBy = problemReportedBy;
    }

    public String getProblemReportDate() {
        return problemReportDate;
    }

    public void setProblemReportDate(String problemReportDate) {
        this.problemReportDate = problemReportDate;
    }


}
