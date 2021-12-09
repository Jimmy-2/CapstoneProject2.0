/**
 * Created by Jimmy.
 * */

package com.example.capstoneproject.fragments.alerts;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.capstoneproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import okhttp3.Headers;


public class AlertsAddFragment extends Fragment {

    TextView tvSymbol;
    public static final String TAG = "AddAlerts"; // testing for logcat

    TextView tvPrice;
    TextView tvName;
    TextView tvPrevious;
    TextView tvChange;
    TextView tvChangePercent;

    EditText etPriceMovement;
    Button btnSetAlert;

    SwipeRefreshLayout swipeContainerAdd;

    //for api
    String stock;

    String symbol = "";
    String stockName = "";
    String currentPrice= "";

    public AlertsAddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alerts_add, container, false);

        stock = getArguments().getString("ticker");


        swipeContainerAdd = view.findViewById(R.id.swipeContainerAdd);

        // Configure the refreshing colors
        swipeContainerAdd.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipeContainerAdd.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                //refresh AlertsAddFragment
                AlertsAddFragment newFragment = new AlertsAddFragment();
                Bundle args = new Bundle();
                args.putString("ticker", stock);
                newFragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.flContainer, newFragment).addToBackStack(null).commit();
                swipeContainerAdd.setRefreshing(false);
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        tvPrice = view.findViewById(R.id.tvPrice);
        tvSymbol = view.findViewById(R.id.tvSymbol);
        tvName = view.findViewById(R.id.tvName);
        tvPrevious = view.findViewById(R.id.tvPrevious);
        tvChange = view.findViewById(R.id.tvChange);
        tvChangePercent = view.findViewById(R.id.tvChangePercent);


        etPriceMovement = view.findViewById(R.id.etPriceMovement);

        etPriceMovement.setFilters(new InputFilter[]{new DigitsInputFilter(6, 2, 999999)});

        getStockPrice(stock);

        btnSetAlert = view.findViewById(R.id.btnSetAlert);
        btnSetAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPriceToDB();


            }
        });
        etPriceMovement.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addPriceToDB();

                }
                return false;
            }
        });


    }

    void addPriceToDB() {
        if(etPriceMovement.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Alert price cannot be empty!", Toast.LENGTH_SHORT).show();
        }else {
            //add a new alert to the database table
            AlertsDatabaseHelper alertDB = new AlertsDatabaseHelper(getActivity());
            alertDB.addAlert(tvSymbol.getText().toString().trim(),
                    tvName.getText().toString().trim(),
                    Double.valueOf(tvPrice.getText().toString().trim()),
                    //200,
                    Double.valueOf(etPriceMovement.getText().toString().trim()));


            /*
            //testing for completed alerts
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
            AlertsCompletedDatabaseHelper alertCompletedDB = new AlertsCompletedDatabaseHelper(getActivity());
            alertCompletedDB.addAlertCompleted(tvSymbol.getText().toString().trim(),
                    tvName.getText().toString().trim(),
                    currentDateTimeString ,
                    Double.valueOf(etPriceMovement.getText().toString().trim()));


             */

            //move back to AlertsFragment
            moveToAlertsHome();



        }
    }

    void getStockPrice(String stock) {
        String TEST_API = "https://api.twelvedata.com/quote?symbol="+stock+"&interval=1day&apikey=313fd5808dc2469abf9380853265bca3";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TEST_API, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject results = jsonObject;
                    symbol = results.getString("symbol");
                    stockName = results.getString("name");
                    currentPrice = results.getString("close");
                    String previous = results.getString("previous_close");
                    String change = results.getString("change");
                    String changePercent = results.getString("percent_change");
                    changePercent = changePercent.concat("%");

                    tvSymbol.setText(symbol);
                    tvName.setText(stockName);
                    tvPrice.setText(currentPrice);
                    tvPrevious.setText(previous);
                    tvChange.setText(change);
                    tvChangePercent.setText(changePercent);

                    //change colors based on price change from previous close
                    double changeNum = Double.parseDouble(change);
                    if (changeNum < 0) {
                        tvSymbol.setTextColor(Color.RED);
                        tvPrice.setTextColor(Color.RED);
                        tvPrevious.setTextColor(Color.RED);
                        tvChange.setTextColor(Color.RED);
                        tvChangePercent.setTextColor(Color.RED);
                    }else {
                        tvSymbol.setTextColor(Color.GREEN);
                        tvPrice.setTextColor(Color.GREEN);
                        tvPrevious.setTextColor(Color.GREEN);
                        tvChange.setTextColor(Color.GREEN);
                        tvChange.setText("+"+change);
                        tvChangePercent.setTextColor(Color.GREEN);
                        tvChangePercent.setText("+"+changePercent);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();

                    Toast.makeText(getContext(), "Error, searched stock ticker doesn't exist!", Toast.LENGTH_SHORT).show();

                    //go back due to error
                    moveToAlertsHome();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    void moveToAlertsHome() {
        //pop the back stack so we can no longer go back to this fragment after clicking the back button
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();

        //go to AlertsFragment
        FragmentTransaction fragment = getFragmentManager().beginTransaction();
        fragment.replace(R.id.flContainer, new AlertsFragment()).commit();
    }



}