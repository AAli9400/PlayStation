package com.a.ali.playstation.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Console {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String dev_code;
    private String startTime;

    private String single_multi;
    private String state;

    private String DepositCash;

    @Ignore
    public static final String CONSOLE_STATUS_FINISH = "finish";

    @Ignore
    public static final String CONSOLE_STATUS_PLAYING = "playing";

    @Ignore
    public static final String CONSOLE_STATUS_FINISH_TRANSFORMING = "finish transforming";

    @Ignore
    public static final String CONSOLE_STATUS_SINGLE = "single";

    @Ignore
    public static final String CONSOLE_STATUS_MULTI = "multi";

    public Console() {
    }

    public Console(int id, String dev_code, String startTime, String single_multi, String state, String depositCash) {
        this.id = id;
        this.dev_code = dev_code;
        this.startTime = startTime;
        this.single_multi = single_multi;
        this.state = state;
        DepositCash = depositCash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static String getConsoleStatusFinish() {
        return CONSOLE_STATUS_FINISH;
    }

    public static String getConsoleStatusPlaying() {
        return CONSOLE_STATUS_PLAYING;
    }

    public static String getConsoleStatusSingle() {
        return CONSOLE_STATUS_SINGLE;
    }

    public static String getConsoleStatusMulti() {
        return CONSOLE_STATUS_MULTI;
    }
}
