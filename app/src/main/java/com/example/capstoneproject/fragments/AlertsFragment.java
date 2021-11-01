package com.example.capstoneproject.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.capstoneproject.AlertsAdapter;
import com.example.capstoneproject.AlertsDatabaseHelper;
import com.example.capstoneproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import okhttp3.Headers;


public class AlertsFragment extends Fragment {
    public static final String TAG = "HomeAlerts"; // testing for logcat

    static String searchedStock = "";
    AlertsDatabaseHelper alertDB;
    ArrayList<String> alert_id, symbol, name, currentPrice, alertPrice;
    AlertsAdapter alertsAdapter;

    SwipeRefreshLayout swipeContainer;
    RecyclerView recyclerView;
    ImageButton btnSearchAlert;
    EditText etSearchAlert;

    //using hashset to get unique stock tickers
    //HashSet<String> tickerSet = new HashSet<String>();


    //Using hashtable to store new stock prices as values and the stock ticker symbols as the keys
    Hashtable<String, String> alertHashTable = new Hashtable<String, String>();

    String batchTicker = "";



    public AlertsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_alerts, container, false);

        swipeContainer = view.findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewCurrentPrice();
                swipeContainer.setRefreshing(false);
            }
        });

        etSearchAlert = view.findViewById(R.id.etSearchAlert);
        btnSearchAlert = view.findViewById(R.id.btnSearchAlert);
        btnSearchAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddFragment();
            }
        });

        etSearchAlert.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Toast.makeText(getContext(), etSearchAlert.getText().toString(), Toast.LENGTH_SHORT).show();
                    goToAddFragment();

                }
                return false;
            }
        });



        return view;

    }

    void goToAddFragment() {
        searchedStock = etSearchAlert.getText().toString();
        AlertsAddFragment newFragment = new AlertsAddFragment();

        //using bundle to pass data to another fragment
        Bundle args = new Bundle();
        //key, value
        args.putString("ticker", searchedStock );
        newFragment.setArguments(args);
        //add a stack so we can click back button to go back
        getFragmentManager().beginTransaction().replace(R.id.flContainer, newFragment).addToBackStack(null).commit();
    }

    void goToGraphFragment(String stockTicker) {
        StockGraphFragment newFragment = new StockGraphFragment();

        //using bundle to pass data to another fragment
        Bundle args = new Bundle();
        //key, value
        args.putString("ticker", stockTicker);
        newFragment.setArguments(args);
        //add a stack so we can click back button to go back
        getFragmentManager().beginTransaction().replace(R.id.flContainer, newFragment).addToBackStack(null).commit();
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvAlerts);


        alertDB = new AlertsDatabaseHelper(getActivity());
        alert_id = new ArrayList<>();
        symbol = new ArrayList<>();
        name = new ArrayList<>();
        currentPrice = new ArrayList<>();
        alertPrice = new ArrayList<>();

        //grab data from readAllData() and store it in array
        storeDataInArrays();

        alertsAdapter = new AlertsAdapter(getContext(), alert_id, symbol, name, currentPrice, alertPrice);
        recyclerView.setAdapter(alertsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //getNewCurrentPrice();
    }



    void storeDataInArrays() {
        Cursor cursor = alertDB.readAllData();
        if(cursor.getCount() == 0) {
            //Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }else {
            while(cursor.moveToNext()) {
                alert_id.add(cursor.getString(0));
                symbol.add(cursor.getString(1));
                name.add(cursor.getString(2));
                currentPrice.add(cursor.getString(3));
                alertPrice.add(cursor.getString(4));

            }
        }
    }

    public void getNewCurrentPrice() {
        AlertsDatabaseHelper alertDB = new AlertsDatabaseHelper(getActivity());
        for(int i = 0; i < alertDB.getAlertsCount(); i++) {
            alertHashTable.put(String.valueOf(symbol.get(i)), "0");

            //add each stock ticker from the database into a hashset
            //tickerSet.add(String.valueOf(symbol.get(i)));
        }
        Set<String> keys = alertHashTable.keySet();
        for(String key: keys){
            batchTicker = batchTicker.concat(key+",");
        }
        //System.out.println(batchTicker);
        //batchTicker = TextUtils.join(",",tickerSet);


        //api link to get batch stock prices
        String TEST_API = "https://api.twelvedata.com/price?symbol="+batchTicker+"&apikey=c2c894e47847490993e8704e2fe75dd6";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TEST_API, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                String testing = "";
                try {
                    //for testing
                    //refresh the prices of here

                    for(String key: keys){
                        JSONObject results = jsonObject.getJSONObject(key);
                        String stockPrice = results.getString("price");
                        alertHashTable.put(key, stockPrice);
                        System.out.println(key+stockPrice);
                    }

                    System.out.println(alertHashTable.toString());

                    //count each database item (recyclerview item count) and update it's currentPrice with the new one from the api call. Increment through its position
                    for(int i = 0; i < alertDB.getAlertsCount(); i++) {
                        alertDB.updatePriceDatabase(String.valueOf(alert_id.get(i)), alertHashTable.get(symbol.get(i)));
                    }
                    //restart the current fragment with a new version in order to show the updated data
                    getFragmentManager().beginTransaction().replace(R.id.flContainer, new AlertsFragment()).commit();



                    //String currentPrice = String.valueOf(alertDB.getAlertsCount());
                    // alertDB.updatePriceDatabase(String.valueOf(alert_id.get(0)), alert_id.get(0));

                    /*
                    List<String> objectList = new ArrayList<String>(tickerSet);
                    for(int i = 0; i < objectList.size(); i++) {
                        JSONObject results = jsonObject.getJSONObject(objectList.get(i));
                        String stockPrice = results.getString("price");
                        System.out.println(objectList.get(i)+stockPrice);


                    }

                    */



                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();


                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });






    }
}