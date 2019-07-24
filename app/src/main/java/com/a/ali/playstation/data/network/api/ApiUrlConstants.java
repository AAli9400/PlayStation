package com.a.ali.playstation.data.network.api;

public class ApiUrlConstants {
    public static String BASE_URL = "http://null/PSSwf/WwwService.svc/";

    public static void resetIpAddress(String newIpAddress) {
        BASE_URL = "http://" + newIpAddress + "/PSSwf/WwwService.svc/";
    }

    public static final String LOGIN_URL = "GetAllusers";
    public static final String LOAD_CONSOLES_URL = "GetConsoleLIST";
    public static final String LOAD_ORDERS_URL = "GetminiOrder";
    public static final String PLAY_REPORT_URL = "PlayReport";
    public static final String CAFE_REPORT_URL = "CafeRep";
    public static final String SUMMARY_REPORT_URL = "ShiftSheet";
}