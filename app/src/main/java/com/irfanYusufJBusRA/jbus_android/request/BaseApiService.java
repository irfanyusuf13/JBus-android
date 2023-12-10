package com.irfanYusufJBusRA.jbus_android.request;

import com.irfanYusufJBusRA.jbus_android.model.Account;
import com.irfanYusufJBusRA.jbus_android.model.BaseResponse;
import com.irfanYusufJBusRA.jbus_android.model.Bus;
import com.irfanYusufJBusRA.jbus_android.model.BusType;
import com.irfanYusufJBusRA.jbus_android.model.Facility;
import com.irfanYusufJBusRA.jbus_android.model.Payment;
import com.irfanYusufJBusRA.jbus_android.model.Renter;
import com.irfanYusufJBusRA.jbus_android.model.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;



/**
 * A base API service that contains all the API calls.
 * @author Irfan Yusuf
 */

public interface BaseApiService {

    /**
     * A method that calls the all API paymentcontroller.
     * A method that calls the all API accountcontroller.
     * A method that calls the all api buscontroller.
     * A method that calls the all api stationcontroller.
     */

    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);



    @POST("account/register")
    Call<BaseResponse<Account>> register (
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/login")
    Call<BaseResponse<Account>> login(
            @Query("email") String email,
            @Query("password") String password);

    @POST ("account/{id}/topUp")
    Call<BaseResponse<Double>> topUp(
            @Path("id") int id,
            @Query("amount") double amount);

    @POST("account/{id}/registerRenter")
    Call<BaseResponse<Renter>> registerRenter(
            @Path("id") int id,
            @Query("companyName") String companyName,
            @Query("address") String address,
            @Query("phoneNumber") String phoneNumber);

    @GET("station/getAll")
    Call<List<Station>> getAllStation();

    @GET("bus/getMyBus")
    Call<List<Bus>> getMyBus(
            @Query("accountId") int accountId
    );

    @POST("bus/create")
    Call<BaseResponse<Bus>> create(
            @Query("accountId") int accountId,
            @Query("name") String name,
            @Query("capacity") int capacity,
            @Query("facilities") List<Facility> facilities,
            @Query("busType") BusType busType,
            @Query("price") int price,
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId
    );

    @POST("bus/addSchedule")
    Call<BaseResponse<Bus>> addSchedule(
            @Query("busId") int busId,
            @Query("time") String time
    );

    @GET("bus/page")
    Call<List<Bus>> getBus(
            @Query("page") int page,
            @Query("size") int pageSize
    );

    @GET("bus/total")
    Call<Integer> numberOfBuses();


    @GET("bus/{id}")
    Call<Bus> getBusbyId(
            @Path("id") int busId
    );

    @GET("bus/getAllBus")
    Call<List<Bus>> getAllBus();

    @POST("Payment/makeBooking")
    Call<BaseResponse<Payment>> makeBooking(
            @Query("buyerId") int buyerId,
            @Query("renterId") int renterId,
            @Query("busId") int busId,
            @Query("busSeats") List <String> busSeats,
            @Query("departureDate") String departureDate
    );


}
