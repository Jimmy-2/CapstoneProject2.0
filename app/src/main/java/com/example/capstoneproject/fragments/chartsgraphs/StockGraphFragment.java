package com.example.capstoneproject.fragments.chartsgraphs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.capstoneproject.R;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class StockGraphFragment extends Fragment {


    TextView chartTitleTextView, candleTimeTextView, openTextView, closeTextView, volumeTextView, highTextView, lowTextView;
    CandleStickChart candleStickChart;
    Button min1Button, min5Button, min15Button;

    int countOfTime = 0;

    String stockTicker;
    String graphTime;

    public StockGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_graph, container, false);
        stockTicker = getArguments().getString("stockTicker");
        graphTime = getArguments().getString("graphTime");


        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        candleStickChart = view.findViewById(R.id.candlestickchart);
        chartTitleTextView = view.findViewById(R.id.chartTitleTextView);
        candleTimeTextView = view.findViewById(R.id.candleTimeTextView);
        openTextView= view.findViewById(R.id.openTextView);
        closeTextView= view.findViewById(R.id.closeTextView);
        volumeTextView= view.findViewById(R.id.volumeTextView);
        highTextView= view.findViewById(R.id.highTextView);
        lowTextView= view.findViewById(R.id.lowTextView);
        min1Button = view.findViewById(R.id.min1Button);
        min5Button = view.findViewById(R.id.min5Button);
        min15Button = view.findViewById(R.id.min15Button);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) min1Button.getLayoutParams();
        RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) min5Button.getLayoutParams();
        RelativeLayout.LayoutParams params15 = (RelativeLayout.LayoutParams) min15Button.getLayoutParams();
        params1.width = getResources().getDisplayMetrics().widthPixels/3;
        params5.width = getResources().getDisplayMetrics().widthPixels/3;
        params15.width = getResources().getDisplayMetrics().widthPixels/3;
        min1Button.setLayoutParams(params1);
        min5Button.setLayoutParams(params5);
        min15Button.setLayoutParams(params15);

        if(graphTime.equals("1min")) {
            min1Button.setBackgroundColor(Color.parseColor("#546360"));
            System.out.println(graphTime+"HELLO");
        }else if (graphTime.equals("5min")) {
            min5Button.setBackgroundColor(Color.parseColor("#546360"));
            System.out.println(graphTime+"HELLO");
        }else {
            min15Button.setBackgroundColor(Color.parseColor("#546360"));
            System.out.println(graphTime+"HELLO");
        }

        min1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                StockGraphFragment newFragment = new StockGraphFragment();
                Bundle args = new Bundle();
                args.putString("stockTicker", stockTicker);
                args.putString("graphTime", "1min");
                newFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, newFragment).addToBackStack(null).commit();

            }
        });

        min5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                StockGraphFragment newFragment = new StockGraphFragment();
                Bundle args = new Bundle();
                args.putString("stockTicker", stockTicker);
                args.putString("graphTime", "5min");
                newFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, newFragment).addToBackStack(null).commit();



            }
        });

        min15Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                StockGraphFragment newFragment = new StockGraphFragment();
                Bundle args = new Bundle();
                args.putString("stockTicker", stockTicker);
                args.putString("graphTime", "15min");
                newFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, newFragment).addToBackStack(null).commit();

            }
        });


        /*
        YAxis yAxis = candleStickChart.getAxisLeft();
        yAxis.setTextSize(12f);
        yAxis.setAxisMinValue(1); /
        yAxis.setAxisMaxValue(600);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setGranularity(1f);
        yAxis.setLabelCount(6, true);
         */



        ArrayList stockTimeSeriesVals = new ArrayList();
        ArrayList stockDateTimes = new ArrayList();


        ArrayList<String> highPrice = new ArrayList<>();
        ArrayList<String> lowPrice = new ArrayList<>();
        ArrayList<String> openPrice = new ArrayList<>();
        ArrayList<String> closePrice = new ArrayList<>();
        ArrayList<String> volumePrice = new ArrayList<>();
        ArrayList<String> timePrice = new ArrayList<>();

        List<Integer> colors = new ArrayList<>();
        int greeen  = Color.rgb(110,190,102);
        int reed = Color.rgb(211,87,44);

        chartTitleTextView.setText(stockTicker);

        String TEST_API = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+stockTicker+"&interval="+graphTime+"&apikey=UXFCZEY0WC38WLF2&outputsize=full";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TEST_API, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject objects = jsonObject.getJSONObject("Time Series ("+graphTime+")");
                    JSONArray key = objects.names ();
                    countOfTime = key.length();
                    int counter = 0;
                    for (int i = key.length()-1; i >= 0; i--) {

                        String keys = key.getString (i);
                        String value = objects.getString (keys);
                        JSONObject date = objects.getJSONObject(keys);
                        String open = date.getString("1. open");
                        String high = date.getString("2. high");
                        String low = date.getString("3. low");
                        String close = date.getString("4. close");
                        String volume = date.getString("5. volume");

                        highPrice.add(high);
                        lowPrice.add(low);
                        openPrice.add(open);
                        closePrice.add(close);
                        volumePrice.add(volume);
                        timePrice.add(keys);

                        stockTimeSeriesVals.add(new CandleEntry(counter, Float.valueOf(high), Float.valueOf(low), Float.valueOf(open), Float.valueOf(close)));
                        stockDateTimes.add(keys);

                        if (Float.valueOf(close)>=Float.valueOf(open)) {
                            colors.add(greeen);
                        }else {
                            colors.add(reed);
                        }
                        counter++;

                    }


                    CandleDataSet stockCandleSet = new CandleDataSet(stockTimeSeriesVals, "Stock Chart");
                    candleStickChart.animateY(0);
                    CandleData data = new CandleData(stockDateTimes, stockCandleSet);
                    stockCandleSet.setColors(colors);
                    Legend legend = candleStickChart.getLegend();
                    legend.setEnabled(false); // hide legend

                    candleStickChart.setDescription(stockTicker+" " + graphTime.toUpperCase()  + " Chart");
                    candleStickChart.setData(data);

                    String openText = openPrice.get(countOfTime-1);
                    String closeText = closePrice.get(countOfTime-1);
                    String highText = highPrice.get(countOfTime-1);
                    String lowText = lowPrice.get(countOfTime-1);
                    String volumeText = volumePrice.get(countOfTime-1);
                    String timeText = timePrice.get(countOfTime-1);
                    openTextView.setText(openText);
                    closeTextView.setText(closeText);
                    highTextView.setText(highText);
                    lowTextView.setText(lowText);
                    volumeTextView.setText(volumeText);
                    candleTimeTextView.setText("Latest Refreshed Time: "+timeText);

                    candleStickChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

                        @Override
                        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                            int indexText = e.getXIndex();
                            String openText = openPrice.get(indexText);
                            String closeText = closePrice.get(indexText);
                            String highText = highPrice.get(indexText);
                            String lowText = lowPrice.get(indexText);
                            String volumeText = volumePrice.get(indexText);
                            String timeText = timePrice.get(indexText);


                            openTextView.setText(openText);
                            closeTextView.setText(closeText);
                            highTextView.setText(highText);
                            lowTextView.setText(lowText);
                            volumeTextView.setText(volumeText);
                            candleTimeTextView.setText(timeText);

                        }

                        @Override
                        public void onNothingSelected() {
                            String openText = openPrice.get(countOfTime-1);
                            String closeText = closePrice.get(countOfTime-1);
                            String highText = highPrice.get(countOfTime-1);
                            String lowText = lowPrice.get(countOfTime-1);
                            String volumeText = volumePrice.get(countOfTime-1);
                            String timeText = timePrice.get(countOfTime-1);
                            openTextView.setText(openText);
                            closeTextView.setText(closeText);
                            highTextView.setText(highText);
                            lowTextView.setText(lowText);
                            volumeTextView.setText(volumeText);
                            candleTimeTextView.setText("Latest Refreshed Time: "+timeText);

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();



                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
            }
        });



    }
}