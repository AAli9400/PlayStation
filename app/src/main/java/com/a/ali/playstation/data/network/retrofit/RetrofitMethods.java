package com.a.ali.playstation.data.network.retrofit;

import androidx.annotation.NonNull;

import com.a.ali.playstation.data.network.api.ApiParameterConstants;
import com.a.ali.playstation.data.network.api.ApiUrlConstants;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitMethods {
    @FormUrlEncoded
    @POST(ApiUrlConstants.LOGIN_URL)
    Call<String> login(@Field(ApiParameterConstants.USERNAME_PARAMETER_NAME) String userName,
                       @Field(ApiParameterConstants.PASSWORD_PARAMETER_NAME) String password);

    @FormUrlEncoded
    @POST(ApiUrlConstants.LOAD_ROOMS_URL)
    Call<String> loadRooms();

    @FormUrlEncoded
    @POST(ApiUrlConstants.LOAD_ORDERS_URL)
    Call<String> loadOrders(int roomId);

    Call<String> loadReport(int selectedShiftPosition,
                            int checkedReportTypeRadioButtonId,
                            @NonNull Date reportDateFrom,
                            @NonNull Date reportDateTo);
}