package com.irfanYusufJBusRA.jbus_android.request;

import com.irfanYusufJBusRA.jbus_android.model.Account;
import com.irfanYusufJBusRA.jbus_android.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);
}
