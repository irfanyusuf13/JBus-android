package com.irfanYusufJBusRA.jbus_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import com.irfanYusufJBusRA.jbus_android.model.Bus;
import com.irfanYusufJBusRA.jbus_android.model.Station;
import com.irfanYusufJBusRA.jbus_android.request.BaseApiService;
import com.irfanYusufJBusRA.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * an activity that contain the list of bus that renter made.
 * user will to this page after login
 * @author Irfan Yusuf
 */

public class MainActivity extends AppCompatActivity {
    Context mContext;
    BaseApiService mApiService;
    private ListView listView;
    private Button[] btns;

    private int currentPage = 0;
    private int pageSize = 10;
    private int listSize;
    private int noOfPages;
    public static List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    public static int busIndex;


    int current;
    private HorizontalScrollView pageScroll = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        listView = findViewById(R.id.listView);
        getBusList();


        prevButton = findViewById(R.id.previousPage);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);


        paginationFooter();
        goToPage(currentPage);

        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0 ? currentPage - 1 : 0;
            goToPage(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages - 1 ? currentPage + 1 : currentPage;
            goToPage(currentPage);
        });
    }

    /**
     * this method is used to make action bar / option.
     * @author Irfan Yusuf
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    /**
     * this method is used if user click the icon on table
     * if user click human icon it will move to aboutmeactivity
     * if user click wallet icon it will move to paymentactivity
     * @author Irfan Yusuf
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search_button) {
            Toast.makeText(MainActivity.this, "button", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.search_profile) {
            Intent intent = new Intent(this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.search_payment) {
            Toast.makeText(MainActivity.this, "payment", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void paginationFooter() {
        int val = listSize % pageSize;
        val = val == 0 ? 0 : 1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity =
                    Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i] = new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));

            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }

    private void goToPage(int index) {
        for (int i = 0; i < noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));

                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(listBus, currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }

    private void viewPaginatedList(List<Bus> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        BusArrayAdapter paginatedAdapter = new BusArrayAdapter(this, R.layout.bus_view, paginatedList);
        listView.setAdapter(paginatedAdapter);
    }

    /**
     * this method is used get the bus that renter made
     * @author Irfan Yusuf
     */
    private void getBusList() {

        mApiService.getAllBus().enqueue(new Callback<List<Bus>>() {
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Bus> busList = response.body();
                listBus = busList;
                busArrayAdapter arrayAdapter = new busArrayAdapter(mContext,busList);
                listView.setAdapter(arrayAdapter);
                listSize = busList.size();


            }

            public void onFailure(Call<List<Bus>> call, Throwable t) {

            }


        });


    }

    /**
     * this Array Adapter for Bus that take all information
     * of bus from manage bus activity
     * @author Irfan Yusuf
     */

    private class busArrayAdapter extends ArrayAdapter<Bus> {

        public busArrayAdapter(@NonNull Context context, @NonNull List<Bus> busList) {
            super(context, 0, busList);
            mContext = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItemView = convertView;


            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
            }
            Bus currentBus = getItem(position);

            TextView busName = listItemView.findViewById(R.id.textBis);
            busName.setText(currentBus.name);


            ImageView addSched = listItemView.findViewById(R.id.gambarBis);
            addSched.setOnClickListener(v -> {
                Intent i = new Intent(mContext, BusDetailActivity.class);
                busIndex = currentBus.id;
                i.putExtra("busId", currentBus.id);
                mContext.startActivity(i);
            });

            return listItemView;


        }

    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        mContext.startActivity(intent);
    }

}