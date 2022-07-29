package com.example.useraccountmanagement.services;


import com.example.useraccountmanagement.AgentPasswordChange;
import com.example.useraccountmanagement.model.AgentBetlist;
import com.example.useraccountmanagement.model.AgentLogin;
import com.example.useraccountmanagement.model.AgentUserLists;
import com.example.useraccountmanagement.model.Block;
import com.example.useraccountmanagement.model.ChangePassword;
import com.example.useraccountmanagement.model.Credit;
import com.example.useraccountmanagement.model.DetailList;
import com.example.useraccountmanagement.model.Give;
import com.example.useraccountmanagement.model.Listallfinalnumber;
import com.example.useraccountmanagement.model.RegisterActivity;
import com.example.useraccountmanagement.model.Server;
import com.example.useraccountmanagement.model.Take;
import com.example.useraccountmanagement.model.Unblock;
import com.example.useraccountmanagement.model.UserChangePass;
import com.example.useraccountmanagement.model.Userlist;
import com.example.useraccountmanagement.model.Wonlist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiRequest {
    @FormUrlEncoded
    @POST("agent/login")
    Call<AgentLogin> adminLogin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("agent/change.password")
    Call<ChangePassword> changePassword(@Field("email") String email
            , @Field("oldPassword") String oldPassword
            , @Field("newPassword") String newPassowrd
            , @Field("confirmPassword") String confirmPassword
            , @Header("Authorization") String token);

    @POST("user/create")
    Call<RegisterActivity> agentRegister(
            @Body RegisterActivity registerActivity,
            @Header("Authorization") String token);

    @GET("agent/betlist")
    Call<List<DetailList>> getDetailList(
            @Header("Authorization") String apikey
    );

    @GET("user/list")
    Call<Userlist> listOfUser(@Header("Authorization") String token);


    @GET("agent/betlist")
    Call<List<AgentBetlist>> getBetList(
            @Header("Authorization") String apikey
    );

    @GET("server")
    Call<Server> checkserver(@Header("Authorization") String apikey);


    @FormUrlEncoded
    @POST("agent/take")
    Call<Take> takepoints(@Header("Authorization") String apikey, @Field("email") String email, @Field("points") int points);

    @FormUrlEncoded
    @POST("agent/give")
    Call<Give> givepoints(@Header("Authorization") String apikey, @Field("email") String email, @Field("points") int points, @Field("paid") Boolean check);

    @FormUrlEncoded
    @POST("agent/user/change.password")
    Call<UserChangePass> userchangepass(@Header("Authorization") String token,
                                        @Field("email") String email,
                                        @Field("password") String password
    );

    @GET("user/credit/{name}")
    Call<List<Credit>> creditlist(@Header("Authorization") String apikey, @Path("name") String name);

    @GET("agent/wonlist")
    Call<List<Wonlist>> wonlist(@Header("Authorization") String apikey);


    @FormUrlEncoded
    @POST("agent/block.user")
    Call<Block> blockAgent(@Header("Authorization") String token, @Field("email") String email);

    @GET("admin/agent/user/{email}")
    Call<AgentUserLists> agentuserlists(@Header("Authorization") String apikey,
                                        @Path("email") String email);

    @FormUrlEncoded
    @POST("agent/unblock.user")
    Call<Unblock> unblockAgent(@Header("Authorization") String token, @Field("email") String email);

    @GET("final/number")
    Call<List<Listallfinalnumber>> finalnumber(@Header("Authorization") String apikey);

    Call<AgentPasswordChange> changePassword(String youremail, String token, String email);
}


/*
public interface ApiRequest {

    @GET("admin/betlist")
    Call<List<BettingList>> bettingList(@Header("Authorization") String apikey);

    @GET("admin/user/list")
    Call<AllUserLists> alluserlists(@Header("Authorization") String apikey);

    @FormUrlEncoded
    @POST("admin/agent/change.password")
    Call<AgentChangePassword> agentchangepassword(@Header("Authorization") String apikey, @Field("email") String email, @Field("password") String password);

    @GET("admin/agent/user/{email}")
    Call<AgentUserLists> agentuserlists(@Header("Authorization") String apikey,
                                        @Path("email") String email);
}
*/
