package com.irfanYusufJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextView signUp = null;
    private Button loginButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp = findViewById(R.id.signup);
        loginButton = findViewById(R.id.Register);

        signUp.setOnClickListener(v -> {
            moveActivity(this, RegisterActivity.class);
        });
        loginButton.setOnClickListener(v ->{
            moveActivity(this, MainActivity.class);
        });

    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx , cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx , message , Toast.LENGTH_SHORT).show();
    }


}