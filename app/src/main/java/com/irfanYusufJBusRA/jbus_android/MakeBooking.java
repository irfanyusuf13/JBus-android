package com.irfanYusufJBusRA.jbus_android;

import static android.os.Build.VERSION_CODES.M;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.irfanYusufJBusRA.jbus_android.model.BaseResponse;
import com.irfanYusufJBusRA.jbus_android.model.Bus;
import com.irfanYusufJBusRA.jbus_android.model.Payment;
import com.irfanYusufJBusRA.jbus_android.model.Schedule;
import com.irfanYusufJBusRA.jbus_android.request.BaseApiService;
import com.irfanYusufJBusRA.jbus_android.request.UtilsApi;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * an activity that contain the list of bus that renter made.
 * user will to this page after login
 * @author Irfan Yusuf
 */

public class MakeBooking extends AppCompatActivity {
    BaseApiService mApiService;
    Payment payment;
    Context mContext;
    private int busId;
    private Bus bus;
    public static Bus busClick;
    private Spinner schedule;
    private Spinner seats;
    private Button buttonBooking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_booking);
        busClick = MainActivity.listBus.get(MainActivity.busIndex - 1);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        Button btnMakeBooking = findViewById(R.id.btnMakeBooking);
        schedule = findViewById(R.id.available_schedule_list);
        seats = findViewById(R.id.available_seats_list);
        buttonBooking = findViewById(R.id.btnMakeBooking);

        busId = getIntent().getIntExtra("busId", -1);
        getScheduleDetails(busId);
        mContext = this;

        schedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSeats(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMakeBooking();
            }
        });

        updateSchedule();
    }

    private void getScheduleDetails(int busId) {
        Call<Bus> call = mApiService.getBusbyId(busId);
        call.enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bus = response.body();
                    updateSchedule();
                } else {
                    Toast.makeText(MakeBooking.this, "Failed to get schedule", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                Toast.makeText(MakeBooking.this, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSchedule() {
        if (bus != null) {
            List<Schedule> schedules = bus.schedules;
            ArrayList<String> formattedTimestamps = new ArrayList<>();
            for (Schedule schedule : schedules) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedTimestamp = dateFormat.format(schedule.departureSchedule);
                formattedTimestamps.add(formattedTimestamp);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formattedTimestamps);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            schedule.setAdapter(adapter);
        }
    }

    private void updateSeats(int selectedPosition) {
        if (bus != null && selectedPosition >= 0 && selectedPosition < bus.schedules.size()) {
            Schedule selectedSchedule = bus.schedules.get(selectedPosition);
            Map<String, Boolean> seatAvailability = selectedSchedule.seatAvailability;
            List<String> availableSeats = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : seatAvailability.entrySet()) {
                if (entry.getValue()) {
                    availableSeats.add(entry.getKey());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableSeats);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            seats.setAdapter(adapter);
        }
    }

    private void handleMakeBooking() {
        if (bus != null && schedule.getSelectedItemPosition() >= 0 && schedule.getSelectedItemPosition() < bus.schedules.size()) {
            Schedule selectedSchedule = bus.schedules.get(schedule.getSelectedItemPosition());
            String selectedSeat = seats.getSelectedItem().toString();
            if (selectedSchedule.seatAvailability.containsKey(selectedSeat) && selectedSchedule.seatAvailability.get(selectedSeat)) {
                buttonBooking(selectedSchedule, selectedSeat);
            } else {
                Toast.makeText(MakeBooking.this, "Selected seat is not available", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void buttonBooking(Schedule selectedSchedule, String selectedSeat) {
        int buyerId = LoginActivity.loggedAccount.id;
        int renterId = bus.accountId;
        int busId = bus.id;
        List<String> selectedSeats = Arrays.asList(selectedSeat);
        String departureDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectedSchedule.departureSchedule);

        Call<BaseResponse<Payment>> call = mApiService.makeBooking(buyerId, renterId, busId, selectedSeats, departureDate);
        call.enqueue(new Callback<BaseResponse<Payment>>() {
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Payment> bookingResponse = response.body();
                    if (bookingResponse != null && bookingResponse.success) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(MakeBooking.this, "Booking successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MakeBooking.this, "Failed to make a booking", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MakeBooking.this, "Failed to make a booking", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(MakeBooking.this, "Failed to make the booking", Toast.LENGTH_SHORT).show();
            }
        });
    }
}