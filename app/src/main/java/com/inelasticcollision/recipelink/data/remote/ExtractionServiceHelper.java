package com.inelasticcollision.recipelink.data.remote;

import android.util.Log;

import com.inelasticcollision.recipelink.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExtractionServiceHelper {

    private static ExtractionService INSTANCE;

    public static ExtractionService getInstance() {
        if (INSTANCE == null) {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            // Inject api key into query parameters
                            HttpUrl url = chain.request()
                                    .url()
                                    .newBuilder()
                                    .addQueryParameter("key", BuildConfig.REMOTE_SERVICE_API_KEY)
                                    .build();
                            Request request = chain.request()
                                    .newBuilder()
                                    .url(url)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.REMOTE_SERVICE_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            INSTANCE = retrofit.create(ExtractionService.class);
        }

        return INSTANCE;
    }
}
