package com.mahak.order.apiHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mahak.order.BaseActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mahak.order.BaseActivity.baseUrlTracking;
import static com.mahak.order.BaseActivity.setDeviceTokenUrl;


public class ApiClient {
    private static final String BASE_URL = BaseActivity.baseUrlApi;
    private static Retrofit retrofit = null;
    private static Retrofit DeviceTokenRetrofit = null;
    private static Retrofit Signalretrofit = null;
    public static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .build();


    private static OkHttpClient okHttpClientAuth = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("Authorization", BaseActivity.getPrefUserToken()).build();
            return chain.proceed(request);
        }

    }).connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    public static Retrofit orderRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClientAuth)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
    public static Retrofit trackingRetrofitClient() {
        if (Signalretrofit == null) {
            Signalretrofit = new Retrofit.Builder()
                    .baseUrl(baseUrlTracking)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return Signalretrofit;
    }

    public static Retrofit setDeviceTokenRetrofit() {
        if (DeviceTokenRetrofit == null) {
            DeviceTokenRetrofit = new Retrofit.Builder()
                    .baseUrl(setDeviceTokenUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return DeviceTokenRetrofit;
    }
}
