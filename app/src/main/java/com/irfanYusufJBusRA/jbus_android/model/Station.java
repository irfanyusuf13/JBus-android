package com.irfanYusufJBusRA.jbus_android.model;

import androidx.annotation.NonNull;

public class Station extends Serializable {
    public String stationName;
    public City city;
    public String address;

    @NonNull
    @Override
    public String toString() {
        return stationName;
    }
}