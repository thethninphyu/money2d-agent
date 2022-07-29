package com.example.useraccountmanagement.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    public static Retrofit getClient(String link){
        if (retrofit==null){
            retrofit= new Retrofit.Builder()
                    .baseUrl(link)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
