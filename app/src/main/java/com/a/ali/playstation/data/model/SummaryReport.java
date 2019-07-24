package com.a.ali.playstation.data.model;

public class SummaryReport {
    private String CafeVal;
    private String MiniVal;
    private String NetProfit;
    private String PlayVal;
    private String outsVal;

    public SummaryReport() {
    }

    public SummaryReport(String cafeVal, String miniVal, String netProfit, String playVal, String outsVal) {
        CafeVal = cafeVal;
        MiniVal = miniVal;
        NetProfit = netProfit;
        PlayVal = playVal;
        this.outsVal = outsVal;
    }

    public String getCafeVal() {
        return CafeVal;
    }

    public void setCafeVal(String cafeVal) {
        CafeVal = cafeVal;
    }

    public String getMiniVal() {
        return MiniVal;
    }

    public void setMiniVal(String miniVal) {
        MiniVal = miniVal;
    }

    public String getNetProfit() {
        return NetProfit;
    }

    public void setNetProfit(String netProfit) {
        NetProfit = netProfit;
    }

    public String getPlayVal() {
        return PlayVal;
    }

    public void setPlayVal(String playVal) {
        PlayVal = playVal;
    }

    public String getOutsVal() {
        return outsVal;
    }

    public void setOutsVal(String outsVal) {
        this.outsVal = outsVal;
    }
}
