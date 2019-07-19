package com.a.ali.playstation.data.model;

public class CafeReport {
    private String BillCash;
    private String ItemQt;
    private String Room_Table;
    private String Shift_name;
    private String billDate;
    private String billItem;
    private String billNo;
    private String preDiscount;

    public CafeReport() {
    }

    public CafeReport(String billCash, String itemQt, String room_Table, String shift_name, String billDate, String billItem, String billNo, String preDiscount) {
        BillCash = billCash;
        ItemQt = itemQt;
        Room_Table = room_Table;
        Shift_name = shift_name;
        this.billDate = billDate;
        this.billItem = billItem;
        this.billNo = billNo;
        this.preDiscount = preDiscount;
    }

    public String getBillCash() {
        return BillCash;
    }

    public void setBillCash(String billCash) {
        BillCash = billCash;
    }

    public String getItemQt() {
        return ItemQt;
    }

    public void setItemQt(String itemQt) {
        ItemQt = itemQt;
    }

    public String getRoom_Table() {
        return Room_Table;
    }

    public void setRoom_Table(String room_Table) {
        Room_Table = room_Table;
    }

    public String getShift_name() {
        return Shift_name;
    }

    public void setShift_name(String shift_name) {
        Shift_name = shift_name;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillItem() {
        return billItem;
    }

    public void setBillItem(String billItem) {
        this.billItem = billItem;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getPreDiscount() {
        return preDiscount;
    }

    public void setPreDiscount(String preDiscount) {
        this.preDiscount = preDiscount;
    }
}
