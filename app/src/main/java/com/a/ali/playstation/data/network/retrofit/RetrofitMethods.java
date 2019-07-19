package com.a.ali.playstation.data.network.retrofit;

import androidx.annotation.NonNull;

import com.a.ali.playstation.data.model.CafeOrders;
import com.a.ali.playstation.data.model.Console;
import com.a.ali.playstation.data.network.api.ApiParameterConstants;
import com.a.ali.playstation.data.network.api.ApiUrlConstants;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.GET;

public interface RetrofitMethods {
    @GET(ApiUrlConstants.LOGIN_URL)
    Call<String> login(@Path(ApiParameterConstants.USERNAME_PARAMETER_NAME) String userName,
                       @Path(ApiParameterConstants.PASSWORD_PARAMETER_NAME) String password);

    @GET(ApiUrlConstants.LOAD_ROOMS_URL)
    Call<List<Console>> loadConsoles();

    @GET(ApiUrlConstants.LOAD_ORDERS_URL + "/{consoleCode}")
    Call<List<CafeOrders>> loadOrders(@Path(ApiParameterConstants.CONSOLE_CODE_PARAMETER_NAME) String consoleCode);

    Call<String> loadReport(@Path(ApiParameterConstants.USERNAME_PARAMETER_NAME) int selectedShiftPosition,
                            @Path(ApiParameterConstants.USERNAME_PARAMETER_NAME) int checkedReportTypeRadioButtonId,
                            @Path(ApiParameterConstants.USERNAME_PARAMETER_NAME) @NonNull Date reportDateFrom,
                            @Path(ApiParameterConstants.USERNAME_PARAMETER_NAME) @NonNull Date reportDateTo);
}