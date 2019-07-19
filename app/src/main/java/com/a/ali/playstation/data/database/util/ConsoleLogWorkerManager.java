package com.a.ali.playstation.data.database.util;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ConsoleLogWorkerManager extends Worker {
    public ConsoleLogWorkerManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        getApplicationContext().startService(
                new Intent(getApplicationContext(), ConsoleLogService.class));

        return Result.success();
    }
}
