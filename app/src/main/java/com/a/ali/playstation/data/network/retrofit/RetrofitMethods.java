package com.a.ali.playstation.data.network.retrofit;

import com.a.ali.playstation.data.model.CafeOrders;
import com.a.ali.playstation.data.model.Console;
import com.a.ali.playstation.data.model.PlayReport;
import com.a.ali.playstation.data.model.User;
import com.a.ali.playstation.data.network.api.ApiParameterConstants;
import com.a.ali.playstation.data.network.api.ApiUrlConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitMethods {
    @GET(ApiUrlConstants.LOGIN_URL)
    Call<List<User>> getAllUsers();

    @GET(ApiUrlConstants.LOAD_CONSOLES_URL)
    Call<List<Console>> loadConsoles();

    @GET(ApiUrlConstants.LOAD_ORDERS_URL + "/{consoleCode}")
    Call<List<CafeOrders>> loadOrders(String consoleCode);

    @GET(ApiUrlConstants.PLAY_REPORT_URL + "/{shiftName}/{startDate}/{startHour}/{startMinute}/{am_pm_start}/{endDate}/{endHour}/{endMinute}/{am_pm_end}")
    Call<List<PlayReport>> playReport(String shiftName,
                                      String startDate,
                                      String startHour,
                                      String startMinute,
                                      String am_pm_start,
                                      String endDate,
                                      String endHour,
                                      String endMinute,
                                      String am_pm_end);
}