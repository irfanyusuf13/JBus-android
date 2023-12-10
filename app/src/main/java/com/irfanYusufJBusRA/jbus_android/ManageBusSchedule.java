package com.irfanYusufJBusRA.jbus_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.irfanYusufJBusRA.jbus_android.model.BaseResponse;
import com.irfanYusufJBusRA.jbus_android.model.Bus;
import com.irfanYusufJBusRA.jbus_android.model.Schedule;
import com.irfanYusufJBusRA.jbus_android.request.BaseApiService;
import com.irfanYusufJBusRA.jbus_android.request.UtilsApi;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * an activity for renter to manage bus schedule.
 * @author Irfan Yusuf
 */

public class ManageBusSchedule extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private int busID;
    private TextInputEditText dateText = null;
    private ListView scheduleList;
    private ImageView addSchedule;
    private String selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus_schedule);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        scheduleList = this.findViewById(R.id.schedule_list);
        dateText = this.findViewById(R.id.selectedDateText);
        addSchedule = this.findViewById(R.id.add_a_schedule);

        busID = this.getIntent().getIntExtra("busId", -1);

        showSchedule();
        dateText.setOnClickListener(v->showDialog());
        addSchedule.setOnClickListener(v->handleAddSchedule());
    }

    protected void handleAddSchedule() {
        mApiService.addSchedule(busID, selectedDate).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if (!response.isSuccessful()) return;

                List<Schedule> l = response.body().payload.schedules;
                ArrayAdapter<Schedule> adapter = new ArrayAdapter<>(mContext, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, l);
                scheduleList.setAdapter(adapter);




            }


            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {

            }
        });
    }

    protected void showDialog(){
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_date_setter, null);
        TextInputLayout editTextDate = dialogView.findViewById(R.id.editTextDate);
        TextInputLayout editTextTime = dialogView.findViewById(R.id.editTextTime);
        Button saveDate = dialogView.findViewById(R.id.buttonSaveDate);

        AtomicReference<String> theDate = new AtomicReference<>();
        AtomicReference<String> theTime = new AtomicReference<>();

        editTextDate.getEditText().setOnClickListener(t -> {
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Date");
            builder.setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build());
            MaterialDatePicker datePicker = builder.build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                String date = DateFormat.format("yyyy-MM-dd", (long) selection).toString();
                editTextDate.getEditText().setText(date);
                theDate.set(date);
            });
        });

        editTextTime.getEditText().setOnClickListener(t -> {
            MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
            builder.setTitleText("Select Time");
            MaterialTimePicker timePicker = builder.build();
            timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
            timePicker.addOnPositiveButtonClickListener(selection -> {
                String formattedTime = formatTime(""+timePicker.getHour(), ""+timePicker.getMinute());;
                editTextTime.getEditText().setText(formattedTime);
                theTime.set(formattedTime);
            });
        });

        saveDate.setOnClickListener(t -> {
            Toast.makeText(mContext, "Date saved", Toast.LENGTH_SHORT).show();
            selectedDate = theDate.get() + " " + theTime.get();
            dateText.setText(selectedDate);
            dialog.dismiss();
        });
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }

    protected void showSchedule() {
        mApiService.getBusbyId(busID).enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (!response.isSuccessful()) return;

                List<Schedule> l = response.body().schedules;
                ArrayAdapter<Schedule> adapter = new ArrayAdapter<>(mContext, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, l);
                scheduleList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {

            }
        });
    }

    private String formatTime(String hour, String minute) {
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        return hour + ":" + minute + ":00";
    }
}