package com.a.ali.playstation.data.database.util;

import android.app.IntentService;
import android.content.Intent;

import com.a.ali.playstation.data.model.CafeOrder;
import com.a.ali.playstation.data.model.Console;
import com.a.ali.playstation.data.network.api.ApiUrlConstants;
import com.a.ali.playstation.data.network.retrofit.RetrofitMethods;
import com.a.ali.playstation.data.repository.AppDatabaseRepository;
import com.a.ali.playstation.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConsoleLogService extends IntentService {
    public ConsoleLogService() {
        super("ConsoleLogService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppDatabaseRepository databaseRepository = AppDatabaseRepository.getInstance(getApplication());

        RetrofitMethods retrofitMethods =
                new Retrofit.Builder()
                        .baseUrl(ApiUrlConstants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(RetrofitMethods.class);

        retrofitMethods.loadConsoles().enqueue(new Callback<List<Console>>() {
            @Override
            public void onResponse(Call<List<Console>> call, Response<List<Console>> response) {
                List<Console> consoles = response.body();
                if (consoles != null && consoles.size() > 0) {
                    databaseRepository.deleteAllConsolesData();
                    databaseRepository.deleteAllCafeOrders();

                    databaseRepository.insertConsoles(consoles);

                    List<Console> addedConsoles = databaseRepository.selectAllConsoles();

                    for (int i = 0; i < addedConsoles.size(); i++) {
                        Console console = addedConsoles.get(i);

                        retrofitMethods.loadOrders(console.getDev_code())
                                .enqueue(new Callback<List<CafeOrder>>() {
                                    @Override
                                    public void onResponse(Call<List<CafeOrder>> call, Response<List<CafeOrder>> response) {
                                        if (response.body() != null) {
                                            ArrayList<CafeOrder> ordersList = (ArrayList<CafeOrder>) response.body();

                                            AppExecutors.getInstance().executeOnDiskIOThread(() -> {
                                                for (int j = 0; j < ordersList.size(); j++) {
                                                    CafeOrder order = ordersList.get(j);
                                                    order.setConsoleId(console.getId());
                                                    ordersList.set(j,order);
                                                }

                                                databaseRepository.insertAllCafeOrders(ordersList);
                                            });
//
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<CafeOrder>> call, Throwable t) {
                                        //ignore
                                    }
                                });

                    }

                }
                //else do nothing
            }

            @Override
            public void onFailure(Call<List<Console>> call, Throwable t) {
                //do nothing
            }
        });
    }
}