package com.cloudwell.paywell.services.activity.eticket.busticket.model;

public class BoardingAndDropping {
    private String reportingBranchID;
    private String counterName;
    private String reportingTime;
    private String scheduleTime;


    public BoardingAndDropping(String ReportingBrID, String countName, String reportTime, String SchTime) {
        this.reportingBranchID = ReportingBrID;
        this.counterName = countName;
        this.reportingTime = reportTime;
        this.scheduleTime = SchTime;
    }


    public String getReportingBranchID() {
        return reportingBranchID;
    }

    public void setReportingBranchID(String reportingBranchID) {
        this.reportingBranchID = reportingBranchID;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public String getReportingTime() {
        return reportingTime;
    }

    public void setReportingTime(String reportingTime) {
        this.reportingTime = reportingTime;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String toString() {
        return this.counterName;
    }

}
