package com.irfanYusufJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.irfanYusufJBusRA.jbus_android.model.BusType;
import com.irfanYusufJBusRA.jbus_android.model.Facility;
import com.irfanYusufJBusRA.jbus_android.model.Station;

import java.util.ArrayList;
import java.util.List;

public class AddBusActivity extends AppCompatActivity {
    private BusType[] busType = BusType.values();
    private BusType selectedBusType;
    private Spinner busTypeSpinner, departureSpinner, arrivalSpinner;
    private List<Station> stationList = new ArrayList<>();
    private int selectedDeptStationID;
    private int selectedArrStationID;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private List<Facility> selectedFacilities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        departureSpinner = this.findViewById(R.id.departure_dropdown);
        arrivalSpinner = this.findViewById(R.id.arrival_dropdown);
        busTypeSpinner = this.findViewById(R.id.bus_type_dropdown);
        ArrayAdapter adBus = new ArrayAdapter(this, android.R.layout.simple_list_item_1, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeSpinner.setAdapter(adBus);
        AdapterView.OnItemSelectedListener busTypeOISL = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                selectedBusType = busType[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);

        /*stationList = response.body();

        ArrayAdapter deptBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationList);
        deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        departureSpinner.setAdapter(deptBus);

        AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                selectedBusType = busType[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        departureSpinner.setOnItemSelectedListener(deptOISL);

        ArrayAdapter arrBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationList);
        arrBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        arrivalSpinner.setAdapter(arrBus);
        arrivalSpinner.setOnItemSelectedListener(arrOISL);*/



    }
}