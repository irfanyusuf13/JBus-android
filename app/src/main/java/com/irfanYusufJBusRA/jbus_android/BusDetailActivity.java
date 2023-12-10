package com.irfanYusufJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.irfanYusufJBusRA.jbus_android.model.Bus;
import com.irfanYusufJBusRA.jbus_android.model.Facility;

/**
 * an activity to show the detail of bus that renter made.
 * include the detail of (Facility,Bus name , BusType and etc)
 * @author Irfan Yusuf
 */

public class BusDetailActivity extends AppCompatActivity {

    public static Bus busClick;
    ToggleButton ac,wifi,toilet,lcdTv,coolBox,lunch,largeBaggage,electricSocket;
    TextView busNameTextView,capacityTextView,priceTextView,
            departureTextView,arrivalTextView,busTypeTextView;
    Button orderButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * this  is used to handle Bus Detail .
         * it will contain the information of bus according to its id
         * @author Irfan Yusuf
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);

        busClick = MainActivity.listBus.get(MainActivity.busIndex-1);
        busNameTextView = findViewById(R.id.detail_nametext);
        priceTextView = findViewById(R.id.detail_pricetext);
        departureTextView = findViewById(R.id.detail_departuretext);
        arrivalTextView = findViewById(R.id.detail_arrivaltext);
        busTypeTextView = findViewById(R.id.detail_bustypetext);
        ac =findViewById(R.id.detail_AC);
        wifi =findViewById(R.id.detail_WiFi);
        toilet =findViewById(R.id.detail_Toilet);
        lcdTv =findViewById(R.id.detail_LCDTV);
        coolBox =findViewById(R.id.detail_Coolbox);
        lunch =findViewById(R.id.detail_Lunch);
        largeBaggage =findViewById(R.id.detail_LargeBaggage);
        electricSocket =findViewById(R.id.detail_ElectricSocket);
        orderButton = findViewById(R.id.detail_buttonOrder);

        busTypeTextView.setText(busClick.busType.toString());

        if(busClick.facilities.contains(Facility.AC)){
            ac.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.WIFI)){
            wifi.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.TOILET)){
            toilet.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.LCD_TV)){
            lcdTv.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.COOL_BOX)){
            coolBox.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.LUNCH)){
            lunch.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.LARGE_BAGGAGE)){
            largeBaggage.setChecked(true);
        }
        if(busClick.facilities.contains(Facility.ELECTRIC_SOCKET)){
            electricSocket.setChecked(true);
        }

        String price = "Rp. " + busClick.price.price;
        priceTextView.setText(price);
        busNameTextView.setText(busClick.name);
        busTypeTextView.setText(busClick.busType.toString());
        departureTextView.setText( busClick.departure.toString());
        arrivalTextView.setText(busClick.arrival.toString());
        String capacityValue = String.valueOf(busClick.capacity);
        TextView capacityTextView = findViewById(R.id.detail_capacitytext);
        capacityTextView.setText(capacityValue);


        orderButton.setOnClickListener(v ->{
            Intent intent = new Intent(BusDetailActivity.this,MakeBooking.class);
            intent.putExtra("busId", busClick.id);
            startActivity(intent);
        });

    }

}

