package com.a.ali.playstation.data.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.a.ali.playstation.R;
import com.a.ali.playstation.data.network.api.ApiUrlConstants;
import com.a.ali.playstation.data.network.networkUtil.AppNetworkConnectivityUtil;
import com.a.ali.playstation.data.network.retrofit.RetrofitCall;
import com.a.ali.playstation.data.network.retrofit.RetrofitMethods;

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

    private class RetrofitRequest<T> {
        MutableLiveData<T> enqueue(RetrofitCall<T> retrofitCall) {
            MutableLiveData<T> responseLiveData = new MutableLiveData<>();
            if (isDeviceConnectedToTheInternet())
                retrofitCall.getCall().enqueue(new ApiCallback<>(responseLiveData));
            return responseLiveData;
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