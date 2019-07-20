package com.a.ali.playstation.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CafeOrder {
    private int consoleId;
    @PrimaryKey
    @NonNull
    private String CItem;
    private String ItemPrice;
    private String ItemQT;

    public CafeOrder() {
    }

    public CafeOrder(int consoleId, String CItem, String itemPrice, String itemQT) {
        this.consoleId = consoleId;
        this.CItem = CItem;
        ItemPrice = itemPrice;
        ItemQT = itemQT;
    }

    public int getConsoleId() {
        return consoleId;
    }

    public void setConsoleId(int consoleId) {
        this.consoleId = consoleId;
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
