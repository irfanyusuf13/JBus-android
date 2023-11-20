package com.irfanYusufJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {

    private TextView isiDariEmail;
    private TextView isiDariUser;
    private TextView isiDariBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        isiDariUser = findViewById(R.id.isiUser);
        isiDariEmail = findViewById(R.id.isiEmail);
        isiDariBalance = findViewById(R.id.isiBalance);

        isiDariUser.setText("irfan.yusuf");
        isiDariEmail.setText("irfan@ui.ac.id");
        isiDariBalance.setText("IDR 900000000000000");


    }



}