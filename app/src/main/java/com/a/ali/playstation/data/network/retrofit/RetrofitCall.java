package com.a.ali.playstation.data.network.retrofit;

import retrofit2.Call;

public interface RetrofitCall<T> {
    Call<T> getCall();
}
