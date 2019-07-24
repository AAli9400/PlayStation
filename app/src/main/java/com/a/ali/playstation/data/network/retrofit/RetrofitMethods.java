package com.a.ali.playstation.data.network.retrofit;

import com.a.ali.playstation.data.model.CafeOrder;
import com.a.ali.playstation.data.model.CafeReport;
import com.a.ali.playstation.data.model.Console;
import com.a.ali.playstation.data.model.OutsReport;
import com.a.ali.playstation.data.model.PlayReport;
import com.a.ali.playstation.data.model.SummaryReport;
import com.a.ali.playstation.data.model.User;
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
    Call<List<CafeOrder>> loadOrders(@Path("consoleCode") String consoleCode);

    @GET(ApiUrlConstants.PLAY_REPORT_URL + "/{shiftName}/{startDate}/{startHour}/{startMinute}/{am_pm_start}/{endDate}/{endHour}/{endMinute}/{am_pm_end}")
    Call<List<PlayReport>> playReport(@Path("shiftName") String shiftName,
                                      @Path("startDate") String startDate,
                                      @Path("startHour") String startHour,
                                      @Path("startMinute") String startMinute,
                                      @Path("am_pm_start") String am_pm_start,
                                      @Path("endDate") String endDate,
                                      @Path("endHour") String endHour,
                                      @Path("endMinute") String endMinute,
                                      @Path("am_pm_end") String am_pm_end);

    @GET(ApiUrlConstants.CAFE_REPORT_URL + "/{cafeType}/{shiftName}/{startDate}/{startHour}/{startMinute}/{am_pm_start}/{endDate}/{endHour}/{endMinute}/{am_pm_end}")
    Call<List<CafeReport>> cafeReport(@Path("cafeType") String cafeType,
                                      @Path("shiftName") String shiftName,
                                      @Path("startDate") String startDate,
                                      @Path("startHour") String startHour,
                                      @Path("startMinute") String startMinute,
                                      @Path("am_pm_start") String am_pm_start,
                                      @Path("endDate") String endDate,
                                      @Path("endHour") String endHour,
                                      @Path("endMinute") String endMinute,
                                      @Path("am_pm_end") String am_pm_end);

    @GET(ApiUrlConstants.SUMMARY_REPORT_URL + "/{shiftName}/{startDate}/{startHour}/{startMinute}/{am_pm_start}/{endDate}/{endHour}/{endMinute}/{am_pm_end}")
    Call<List<SummaryReport>> summaryReport(@Path("shiftName") String shiftName,
                                            @Path("startDate") String startDate,
                                            @Path("startHour") String startHour,
                                            @Path("startMinute") String startMinute,
                                            @Path("am_pm_start") String am_pm_start,
                                            @Path("endDate") String endDate,
                                            @Path("endHour") String endHour,
                                            @Path("endMinute") String endMinute,
                                            @Path("am_pm_end") String am_pm_end);

    @GET(ApiUrlConstants.OUTS_REPORT_URL + "/{shiftName}/{startDate}/{startHour}/{startMinute}/{am_pm_start}/{endDate}/{endHour}/{endMinute}/{am_pm_end}")
    Call<List<OutsReport>> outsReport(@Path("shiftName") String shiftName,
                                      @Path("startDate") String startDate,
                                      @Path("startHour") String startHour,
                                      @Path("startMinute") String startMinute,
                                      @Path("am_pm_start") String am_pm_start,
                                      @Path("endDate") String endDate,
                                      @Path("endHour") String endHour,
                                      @Path("endMinute") String endMinute,
                                      @Path("am_pm_end") String am_pm_end);
}