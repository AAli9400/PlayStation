package com.a.ali.playstation.data.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.network.api.ApiUrlConstants;
import com.a.ali.playstation.data.network.networkUtil.AppNetworkConnectivityUtil;
import com.a.ali.playstation.data.network.retrofit.RetrofitCall;
import com.a.ali.playstation.data.network.retrofit.RetrofitMethods;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppNetworkRepository extends AppRepository {
    private RetrofitMethods mRetrofitMethods;

    private static AppNetworkRepository mInstance = null;

    public static AppNetworkRepository getInstance(Application mApplication) {
        if (mInstance == null) {
            mInstance = new AppNetworkRepository(mApplication);
        }

        return mInstance;
    }

    private AppNetworkRepository(Application application) {
        super(application);

        mRetrofitMethods = new Retrofit.Builder().baseUrl(ApiUrlConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitMethods.class);
    }

    public LiveData<String> login(@NonNull String userName, @NonNull String password) {
        return new RetrofitRequest<String>().enqueue(() ->
                mRetrofitMethods.login(userName, password));
    }

    public LiveData<String> loadRooms() {
        return new RetrofitRequest<String>().enqueue(() ->
                mRetrofitMethods.loadRooms());
    }

    public LiveData<String> loadOrders(int roomId) {
        return new RetrofitRequest<String>().enqueue(() ->
                mRetrofitMethods.loadOrders(roomId));
    }

    public LiveData<String> loadReport(int selectedShiftPosition, int checkedReportTypeRadioButtonId,@NonNull Date reportDateFrom,@NonNull Date reportDateTo) {
        return new RetrofitRequest<String>().enqueue(() ->
                mRetrofitMethods.loadReport(selectedShiftPosition,checkedReportTypeRadioButtonId,reportDateFrom, reportDateTo));
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