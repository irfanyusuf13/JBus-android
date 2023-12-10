package com.irfanYusufJBusRA.jbus_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.irfanYusufJBusRA.jbus_android.model.Bus;
import com.irfanYusufJBusRA.jbus_android.request.BaseApiService;
import com.irfanYusufJBusRA.jbus_android.request.UtilsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * an activity for renter to manage bus.
 * @author Irfan Yusuf
 */

public class ManageBusActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private ListView myBusListView;
    ImageView addSched;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);

        getSupportActionBar().setTitle("Manage Bus");

        mContext = this;
        mApiService = UtilsApi.getApiService();
        myBusListView = this.findViewById(R.id.my_bus_list_view);

        loadMyBus();
    }



    protected void loadMyBus() {
        mApiService.getMyBus(LoginActivity.loggedAccount.id).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (!response.isSuccessful()) return;

                List<Bus> myBusList = response.body();
                MyArrayAdapter adapter = new MyArrayAdapter(mContext, myBusList);
                myBusListView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {

            }
        });
    }

    private class MyArrayAdapter extends ArrayAdapter<Bus> {

        public MyArrayAdapter(@NonNull Context context, @NonNull List<Bus> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View currentItemView = convertView;


            if (currentItemView == null) {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_bus_view, parent, false);
            }
            Bus currentBus = getItem(position);

            TextView busName = currentItemView.findViewById(R.id.bus_name);
            busName.setText(currentBus.name);

            ImageView addSched = currentItemView.findViewById(R.id.manage_schedule);
            addSched.setOnClickListener(v->{
                Intent i = new Intent(mContext, ManageBusSchedule.class);
                i.putExtra("busId", currentBus.id);
                mContext.startActivity(i);
            });

            return currentItemView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_managebus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.addScheduleButton) {
            Intent intent = new Intent(mContext, AddBusActivity.class);
            intent.putExtra("type", "addBus");
            startActivity(intent);
            return true;
        } else return super.onOptionsItemSelected(item);
    }

}