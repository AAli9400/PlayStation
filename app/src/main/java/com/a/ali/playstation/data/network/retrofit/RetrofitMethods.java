package com.a.ali.playstation.data.network.retrofit;

import com.a.ali.playstation.data.network.api.ApiParameterConstants;
import com.a.ali.playstation.data.network.api.ApiUrlConstants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitMethods {
    @FormUrlEncoded
    @POST(ApiUrlConstants.GET_URL)
    Call<Integer> get(@Field(ApiParameterConstants.TOKEN_PARAMETER_NAME) String token);
}