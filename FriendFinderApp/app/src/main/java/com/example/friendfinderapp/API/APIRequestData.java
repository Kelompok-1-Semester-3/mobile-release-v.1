package com.example.friendfinderapp.API;

import com.example.friendfinderapp.Category;
import com.example.friendfinderapp.Model.CategoryModel;
import com.example.friendfinderapp.Model.Event_Model;
import com.example.friendfinderapp.Model.ResponseModel;
import com.example.friendfinderapp.Model.UserAccount;
import com.example.friendfinderapp.Model.User_Model;
import com.example.friendfinderapp.userEvent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIRequestData {

    @FormUrlEncoded
    @POST("API/getUserByEmail")
    Call<ResponseModel> ardGetDataUserByEmail(
            @Field("email") String email
    );


    @FormUrlEncoded
    @POST("API/register")
    Call<ResponseModel> ardAddData(
            @Field("fullname") String fullname,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("API/login")
    Call<ResponseModel> resLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("API/getAllUserEvent/{id}")
    Call<List<Event_Model>> resAllUserEvent(@Path("id") String id);

    @GET("API/getEventByKeyword/{keyword}")
    Call<List<Event_Model>> resEventByKeyword(@Path("keyword") String keyword);

    @GET("API/getAllCategories")
    Call<List<CategoryModel>> resGetAllCategories();

    @GET("API/getDetailEvent/{id}")
    Call<userEvent> resGetDetailEvent(@Path("id") String id);

    @GET("API/getDetailAccount/{id}")
    Call<UserAccount> resGetDetailAccount(@Path("id") String id);

     @FormUrlEncoded
    @POST("API/UserEventDestroy")
    Call<ResponseModel> DistroyUserEvent(
            @Field("id") int id
    );

}