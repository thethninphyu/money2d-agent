package com.example.useraccountmanagement.services;

public class ApiUtil {

    public static String link="http://54.169.230.116/money2d/v1/";

    public static ApiRequest getApiRequest(){
        return RetrofitClient.getClient(link).create(ApiRequest.class);
    }
}
