package com.irfanYusufJBusRA.jbus_android.model;

import java.sql.Timestamp;

public class Invoice extends Serializable {

    public int buyerId;
    public int renterId;
    public BusRating rating;
    public Timestamp time;
    public PaymentStatus status;
    public  enum BusRating{
        NONE,
        NEUTRAL,
        GOOD,
        BAD;
    }
    public enum PaymentStatus{
        FAILED,
        WAITING,
        SUCCESS;
    }
}

