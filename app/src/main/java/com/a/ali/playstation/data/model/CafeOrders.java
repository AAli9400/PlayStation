package com.a.ali.playstation.data.model;

public class CafeOrders {
    private String CItem;
    private String ItemPrice;
    private String ItemQT;

    public CafeOrders() {
    }

    public CafeOrders(String CItem, String itemPrice, String itemQT) {
        this.CItem = CItem;
        ItemPrice = itemPrice;
        ItemQT = itemQT;
    }

    public String getCItem() {
        return CItem;
    }

    public void setCItem(String CItem) {
        this.CItem = CItem;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemQT() {
        return ItemQT;
    }

    public void setItemQT(String itemQT) {
        ItemQT = itemQT;
    }
}
