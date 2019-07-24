package com.a.ali.playstation.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CafeOrder {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String CItem;
    private String ItemPrice;
    private String ItemQT;
    private String deviceCode;

    public CafeOrder() {
    }

    public CafeOrder(int id, String CItem, String itemPrice, String itemQT, String deviceCode) {
        this.id = id;
        this.CItem = CItem;
        ItemPrice = itemPrice;
        ItemQT = itemQT;
        this.deviceCode = deviceCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
