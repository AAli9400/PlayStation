package com.a.ali.playstation.data.network.api;

public class ApiUrlConstants {
    public static String BASE_URL = "http://null/PSSwf/WwwService.svc/";

    public static void resetIpAddress(String newIpAddress) {
        BASE_URL = "http://" + newIpAddress + "/PSSwf/WwwService.svc/";
    }

    public static final String LOGIN_URL = "GetAllusers";
    public static final String LOAD_ROOMS_URL = "GetConsoleLIST";
    public static final String LOAD_ORDERS_URL = "GetminiOrder";
}