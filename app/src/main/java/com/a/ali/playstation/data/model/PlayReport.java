package com.a.ali.playstation.data.model;

public class PlayReport {
    private String BillCash;
    private String Finish_date;
    private String Shift_name;
    private String Start_date;
    private String preDiscount;
    private String ps;
    private String single_multi;

    public PlayReport() {
    }

    public PlayReport(String billCash, String finish_date, String shift_name, String start_date, String preDiscount, String ps, String single_multi) {
        BillCash = billCash;
        Finish_date = finish_date;
        Shift_name = shift_name;
        Start_date = start_date;
        this.preDiscount = preDiscount;
        this.ps = ps;
        this.single_multi = single_multi;
    }

    public String getBillCash() {
        return BillCash;
    }

    public void setBillCash(String billCash) {
        BillCash = billCash;
    }

    public String getFinish_date() {
        return Finish_date;
    }

    public void setFinish_date(String finish_date) {
        Finish_date = finish_date;
    }

    public String getShift_name() {
        return Shift_name;
    }

    public void setShift_name(String shift_name) {
        Shift_name = shift_name;
    }

    public String getStart_date() {
        return Start_date;
    }

    public void setStart_date(String start_date) {
        Start_date = start_date;
    }

    public String getPreDiscount() {
        return preDiscount;
    }

    public void setPreDiscount(String preDiscount) {
        this.preDiscount = preDiscount;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getSingle_multi() {
        return single_multi;
    }

    public void setSingle_multi(String single_multi) {
        this.single_multi = single_multi;
    }
}
