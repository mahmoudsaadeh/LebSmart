package com.example.lebsmart.ReportProblemsFragments;

public class Problem {

    String problemType;//category
    String problemDescription;
    String problemReportedBy;
    String problemReportDate;

    boolean expanded;

    public Problem(String problemType, String problemDescription, String problemReportedBy, String problemReportDate) {
        this.problemType = problemType;
        this.problemDescription = problemDescription;
        this.problemReportedBy = problemReportedBy;
        this.problemReportDate = problemReportDate;

        this.expanded = false;
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