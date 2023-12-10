package com.irfanYusufJBusRA.jbus_android;

import static com.irfanYusufJBusRA.jbus_android.LoginActivity.loggedAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.irfanYusufJBusRA.jbus_android.model.Account;
import com.irfanYusufJBusRA.jbus_android.model.Renter;
import com.irfanYusufJBusRA.jbus_android.request.BaseApiService;
import com.irfanYusufJBusRA.jbus_android.request.UtilsApi;
import com.irfanYusufJBusRA.jbus_android.model.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * an activity to user register account as renter
 * @author Irfan Yusuf
 */
public class RegisterRenterActivity extends AppCompatActivity {

    private Context mContext;
    private BaseApiService mApiService;
    private Button companyButton;

    private EditText companyName;
    private EditText address;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);

        mContext=this;
        mApiService = UtilsApi.getApiService();
        companyName = findViewById(R.id.CompanyName);
        address = findViewById(R.id.AddressCompany);
        phoneNumber = findViewById(R.id.PhoneNumberCompany);
        companyButton = findViewById(R.id.registerCompanyButton);

        companyButton.setOnClickListener(v -> {
            handleRegisterCompany();
        });
    }


    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    /**
     * this method is used to handleregiterCompany the login from user.
     * if the companyname / address / phonenumber is empty user cannot regis
     * if register success, user will back to aboutmeactvity
     * @author Irfan Yusuf
     */

    protected void handleRegisterCompany(){
        String companyNew = companyName.getText().toString();
        String addressNew = address.getText().toString();
        String phoneNumNew = phoneNumber.getText().toString();


        if (companyNew.isEmpty() || addressNew.isEmpty() || phoneNumNew.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.registerRenter(LoginActivity.loggedAccount.id, companyNew, addressNew, phoneNumNew).enqueue(new Callback<BaseResponse<Renter>>() {

            @Override
            public void onResponse(Call<BaseResponse<Renter>> call, Response<BaseResponse<Renter>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Renter> res = response.body();

                if (res.success) {
                    LoginActivity.loggedAccount.company =res.payload;
                    moveActivity(mContext, AboutMeActivity.class);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    }
