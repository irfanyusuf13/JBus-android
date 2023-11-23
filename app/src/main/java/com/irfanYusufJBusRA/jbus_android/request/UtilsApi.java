package com.irfanYusufJBusRA.jbus_android.request;



public class UtilsApi {
    public static final String BASE_URL_API = "http://10.0.2.2:5000/";

    public static BaseApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }

}


