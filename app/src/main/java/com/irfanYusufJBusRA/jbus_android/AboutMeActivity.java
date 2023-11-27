package com.irfanYusufJBusRA.jbus_android;

import static com.irfanYusufJBusRA.jbus_android.LoginActivity.loggedAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.irfanYusufJBusRA.jbus_android.model.Account;
import com.irfanYusufJBusRA.jbus_android.model.BaseResponse;
import com.irfanYusufJBusRA.jbus_android.request.BaseApiService;
import com.irfanYusufJBusRA.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {

    TextView isiDariEmail = null;
    TextView isiDariUser = null;
   TextView isiDariBalance = null;

   private EditText topUpAmount;

    private BaseApiService mApiService;
    private Context mContext;
    private Button topUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        mContext = this;
        mApiService = UtilsApi.getApiService();
        isiDariUser = findViewById(R.id.isiUser);
        isiDariEmail = findViewById(R.id.isiEmail);
        isiDariBalance = findViewById(R.id.isiBalance);


        isiDariUser.setText(loggedAccount.name);
        isiDariEmail.setText(loggedAccount.email);
        Double strdouble = new Double(loggedAccount.balance);
        isiDariBalance.setText(strdouble.toString());
        topUpAmount = findViewById(R.id.Number);
        topUpButton = findViewById(R.id.button);
        topUpButton.setOnClickListener(v -> handleTopUp());

        if (LoginActivity.loggedAccount.company != null) {
            TextView textView = findViewById(R.id.status);
            textView.setText("You're Already Registered as a Renter");

            TextView registerRenter = findViewById(R.id.companyRegister);
            registerRenter.setVisibility(View.GONE);


            Button manageBusButton = findViewById(R.id.anotherButton);
            manageBusButton.setOnClickListener(v -> {moveActivity(mContext, ManageBusActivity.class);
            });
        } else {
            TextView textView = findViewById(R.id.status);
            textView.setText("You're Not Registered as a Renter");

            Button manageBusButton = findViewById(R.id.anotherButton);
            manageBusButton.setVisibility(View.GONE);

            TextView registerCompany = findViewById(R.id.companyRegister);
            registerCompany.setOnClickListener(v -> {moveActivity(mContext, RegisterRenterActivity.class);
            });
        }



    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    protected void handleTopUp(){
        String topUpAmountS = topUpAmount.getText().toString();
        if (topUpAmountS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        double topUpD = Double.valueOf(topUpAmountS);
        mApiService.topUp(loggedAccount.id, topUpD).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Double> res = response.body();
                // if success finish this activity
                if (res.success){
                    finish();
                    loggedAccount.balance += res.payload;
                    startActivity(getIntent());
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}