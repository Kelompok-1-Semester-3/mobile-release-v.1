package com.example.friendfinderapp.API;

import com.example.friendfinderapp.Constants.ConfigurationAll;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {


    public static Retrofit retrofit;

    public static Retrofit konekRetro(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(ConfigurationAll.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
