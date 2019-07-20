package com.a.ali.playstation.data.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.model.CafeOrder;
import com.a.ali.playstation.data.model.CafeReport;
import com.a.ali.playstation.data.model.Console;
import com.a.ali.playstation.data.model.PlayReport;
import com.a.ali.playstation.data.model.User;
import com.a.ali.playstation.data.network.api.ApiUrlConstants;
import com.a.ali.playstation.data.network.networkUtil.AppNetworkConnectivityUtil;
import com.a.ali.playstation.data.network.retrofit.RetrofitCall;
import com.a.ali.playstation.data.network.retrofit.RetrofitMethods;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppNetworkRepository extends AppRepository {
    private RetrofitMethods mRetrofitMethods;

    private static AppNetworkRepository mInstance = null;

    public static synchronized AppNetworkRepository getInstance(Application mApplication) {
        if (mInstance == null) {
            mInstance = new AppNetworkRepository(mApplication);
        }

        return mInstance;
    }

    private AppNetworkRepository(Application application) {
        super(application);

        ApiUrlConstants.resetIpAddress(application.getSharedPreferences(
                application.getResources().getString(R.string.ip_shared_preference_name),
                Context.MODE_PRIVATE
        ).getString(application.getResources().getString(R.string.ip_key), null));

        Log.v("djabfjdabf", ApiUrlConstants.BASE_URL);

        mRetrofitMethods = new Retrofit.Builder().baseUrl(ApiUrlConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitMethods.class);
    }

    public LiveData<List<User>> getAllUsers() {
        return new RetrofitRequest<List<User>>().enqueue(() ->
                mRetrofitMethods.getAllUsers());
    }

    public LiveData<List<Console>> loadConsoles() {
        return new RetrofitRequest<List<Console>>().enqueue(() ->
                mRetrofitMethods.loadConsoles());
    }

    public LiveData<List<CafeOrder>> loadOrders(String consoleCode) {
        return new RetrofitRequest<List<CafeOrder>>().enqueue(() ->
                mRetrofitMethods.loadOrders(consoleCode));
    }

    public void resetIp(@NonNull String newIpAddress) {
        ApiUrlConstants.resetIpAddress(newIpAddress);

        mRetrofitMethods = new Retrofit.Builder().baseUrl(ApiUrlConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitMethods.class);
    }

    public LiveData<List<PlayReport>> playReport(@NonNull String shiftName,
                                                 @NonNull String startDate,
                                                 @NonNull String startHour,
                                                 @NonNull String startMinute,
                                                 @NonNull String am_pm_start,
                                                 @NonNull String endDate,
                                                 @NonNull String endHour,
                                                 @NonNull String endMinute,
                                                 @NonNull String am_pm_end) {
        return new RetrofitRequest<List<PlayReport>>().enqueue(() ->
                mRetrofitMethods.playReport(shiftName,
                        startDate, startHour, startMinute, am_pm_start,
                        endDate, endHour, endMinute, am_pm_end));
    }

    public LiveData<List<CafeReport>> cafeReport(@NonNull String cafeType,
                                                 @NonNull String shiftName,
                                                 @NonNull String startDate,
                                                 @NonNull String startHour,
                                                 @NonNull String startMinute,
                                                 @NonNull String am_pm_start,
                                                 @NonNull String endDate,
                                                 @NonNull String endHour,
                                                 @NonNull String endMinute,
                                                 @NonNull String am_pm_end) {
        return new RetrofitRequest<List<CafeReport>>().enqueue(() ->
                mRetrofitMethods.cafeReport(cafeType, shiftName,
                        startDate, startHour, startMinute, am_pm_start,
                        endDate, endHour, endMinute, am_pm_end));
    }

    private class RetrofitRequest<T> {
        MutableLiveData<T> enqueue(RetrofitCall<T> retrofitCall) {
            MutableLiveData<T> responseLiveData = new MutableLiveData<>();
            if (isDeviceConnectedToTheInternet())
                retrofitCall.getCall().enqueue(new ApiCallback<>(responseLiveData));
            return responseLiveData;
        }
    }

    protected class ApiCallback<T> implements Callback<T> {
        private MutableLiveData<T> mResponse;
        private final String TAG = ApiCallback.class.getSimpleName();

        public ApiCallback(MutableLiveData<T> response) {
            super();
            mResponse = response;
        }

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
            if (response.body() == null) {
                Log.e(TAG, response.code() + " // " + response.message());
                mAppExecutors.executeOnMainThread(() -> Toast.makeText(
                        mApplication,
                        response.code() + " // " + response.message(),
                        Toast.LENGTH_SHORT
                ).show());
            }

            mResponse.postValue(response.body());
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            Log.e(TAG, t.getMessage());
            mAppExecutors.executeOnMainThread(() -> Toast.makeText(
                    mApplication,
                    t.getMessage(),
                    Toast.LENGTH_SHORT
            ).show());

            mResponse.postValue(null);
        }
    }

    private boolean isDeviceConnectedToTheInternet() {
        boolean isConnected = AppNetworkConnectivityUtil.isDeviceConnectedToTheInternet(mApplication);
        if (!isConnected) {
            mAppExecutors.executeOnMainThread((() -> Toast.makeText(
                    mApplication,
                    mApplication.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
            ).show()));
        }
        return isConnected;
    }
}