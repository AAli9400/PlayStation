package com.a.ali.playstation.data.model;

public class User {
    private String PW;
    private String  UserN;
    private String UserTitle;

    public static final String TITLE_MANAGER = "manager";
    public static final String TITLE_SHIFT = "shift";

    public User() {
    }

    public User(String PW, String userN, String userTitle) {
        this.PW = PW;
        UserN = userN;
        UserTitle = userTitle;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public String getUserN() {
        return UserN;
    }

    public void setUserN(String userN) {
        UserN = userN;
    }

    public String getUserTitle() {
        return UserTitle;
    }

    public void setUserTitle(String userTitle) {
        UserTitle = userTitle;
    }

    public static String getTitleManager() {
        return TITLE_MANAGER;
    }

    public static String getTitleShift() {
        return TITLE_SHIFT;
    }
}
