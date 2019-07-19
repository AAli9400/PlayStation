package com.a.ali.playstation.data.network.api;

public class ApiUrlConstants {
    public static String IP_ADDRESS = null;
    public static final String BASE_URL = "http://" + IP_ADDRESS + "/PSSwf/WwwService.svc/";

    public static final String LOGIN_URL = "TODO";
    public static final String LOAD_ROOMS_URL = BASE_URL + "GetConsoleLIST";
    public static final String LOAD_ORDERS_URL = "GetminiOrder";
}