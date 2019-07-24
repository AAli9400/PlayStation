package com.a.ali.playstation.data.model;

public class CafeReport {
    private String BillCash;
    private String ItemQT;
    private String Room_Table;
    private String Shift_name;
    private String billDate;
    private String billItem;
    private String billNO;
    private String preDiscount;

    public CafeReport() {
    }

    public CafeReport(String billCash, String itemQT, String room_Table, String shift_name, String billDate, String billItem, String billNO, String preDiscount) {
        BillCash = billCash;
        ItemQT = itemQT;
        Room_Table = room_Table;
        Shift_name = shift_name;
        this.billDate = billDate;
        this.billItem = billItem;
        this.billNO = billNO;
        this.preDiscount = preDiscount;
    }

    public String getBillCash() {
        return BillCash;
    }

    public void setBillCash(String billCash) {
        BillCash = billCash;
    }

    public String getItemQT() {
        return ItemQT;
    }

    public void setItemQT(String itemQT) {
        ItemQT = itemQT;
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

    public String getBillNO() {
        return billNO;
    }

    public void setBillNO(String billNO) {
        this.billNO = billNO;
    }

    public String getPreDiscount() {
        return preDiscount;
    }

    public void setPreDiscount(String preDiscount) {
        this.preDiscount = preDiscount;
    }
}
