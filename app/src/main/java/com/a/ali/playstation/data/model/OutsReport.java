package com.a.ali.playstation.data.model;

public class OutsReport {
    private String Shift_name;
    private String outsDate;
    private String outsNote;
    private String outsType;
    private String outsVal;

    public OutsReport() {
    }

    public OutsReport(String shift_name, String outsDate, String outsNote, String outsType, String outsVal) {
        Shift_name = shift_name;
        this.outsDate = outsDate;
        this.outsNote = outsNote;
        this.outsType = outsType;
        this.outsVal = outsVal;
    }

    public String getShift_name() {
        return Shift_name;
    }

    public void setShift_name(String shift_name) {
        Shift_name = shift_name;
    }

    public String getOutsDate() {
        return outsDate;
    }

    public void setOutsDate(String outsDate) {
        this.outsDate = outsDate;
    }

    public String getOutsNote() {
        return outsNote;
    }

    public void setOutsNote(String outsNote) {
        this.outsNote = outsNote;
    }

    public String getOutsType() {
        return outsType;
    }

    public void setOutsType(String outsType) {
        this.outsType = outsType;
    }

    public String getOutsVal() {
        return outsVal;
    }

    public void setOutsVal(String outsVal) {
        this.outsVal = outsVal;
    }
}
