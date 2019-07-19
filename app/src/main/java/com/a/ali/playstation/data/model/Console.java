package com.a.ali.playstation.data.model;

public class Console {
    private String dev_code;
    private String startTime;
    private String single_multi;
    private String state;
    private String DepositCash;

    public Console() {
    }

    public Console(String dev_code, String startTime, String single_multi, String state, String depositCash) {
        this.dev_code = dev_code;
        this.startTime = startTime;
        this.single_multi = single_multi;
        this.state = state;
        DepositCash = depositCash;
    }

    public String getDev_code() {
        return dev_code;
    }

    public void setDev_code(String dev_code) {
        this.dev_code = dev_code;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSingle_multi() {
        return single_multi;
    }

    public void setSingle_multi(String single_multi) {
        this.single_multi = single_multi;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDepositCash() {
        return DepositCash;
    }

    public void setDepositCash(String depositCash) {
        DepositCash = depositCash;
    }
}
