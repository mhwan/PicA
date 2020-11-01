package com.picaproject.pica.Util;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitRetryableCallback<T> implements Callback<T> {
    private int totalRetries = 3;
    private static final String TAG = RetrofitRetryableCallback.class.getSimpleName();
    private final Call<T> call;
    private int retryCount = 0;

    public RetrofitRetryableCallback(Call<T> call, int totalRetries) {
        this.call = call;
        this.totalRetries = totalRetries;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if (response.isSuccessful())
            onFinalResponse(call, response);
        else {
            if (retryCount++ < totalRetries) {
                Log.v(TAG, "Retrying API Call - (" + retryCount + " / " + totalRetries + ")");
                retry();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG, t.getMessage());
        if (retryCount++ < totalRetries) {
            Log.v(TAG, "Retrying API Call - (" + retryCount + " / " + totalRetries + ")");
            retry();
        } else onFinalFailure(call, t);
    }

    public void onFinalResponse(Call<T> call, Response<T> response) {
    }

    public void onFinalFailure(Call<T> call, Throwable t) {
    }

    private void retry() {
        call.clone().enqueue(this);
    }
}