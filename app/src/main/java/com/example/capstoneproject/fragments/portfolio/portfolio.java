package com.example.capstoneproject.fragments.portfolio;

import static android.icu.lang.UCharacter.toUpperCase;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.capstoneproject.AlertsAdapter;
import com.example.capstoneproject.AlertsDatabaseHelper;
import com.example.capstoneproject.R;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import okhttp3.Headers;


public class portfolio extends Fragment {


    //Graphs - Ariq
    TextView chartTitleTextView2;
    LineChart portfolioChart;
    // - end

    //used for the popup menu
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private EditText popup_stockname, popup_stockamount;
    Button popup_savebutton, popup_cancelbutton;
    Vector<Integer> stockamounts = new Vector<Integer>();
    Vector<String> stocknames = new Vector<String>();

    databaseforportfoliograph portfolioDB;
    myportfoliodatabase myDB;
    databaseforsecondchartportfolio mysecondDB;
    ArrayList<String> book_id, book_title, book_author, book_pages;

    FloatingActionButton gotofragment2; //possibly going to be useless

    Button testbutton;
    Button testbutton2;
    //for recyclerview
    private ArrayList<portfoliostock> stocksnames;
    portfoliostockrecycleradapter portfoliostockadapter;
    private RecyclerView recyclerview;

    public portfolio() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        gotofragment2 = view.findViewById(R.id.addstockcryptobutton);
        myDB = new myportfoliodatabase(getActivity());
        portfolioDB = new databaseforportfoliograph(getActivity());
        //uncomment for actual release of the app ig
        //updatestock();
        mysecondDB = new databaseforsecondchartportfolio(getActivity());
        book_id = new ArrayList<>();
        book_author = new ArrayList<>();
        book_title = new ArrayList<>();
        book_pages = new ArrayList<>();
        testbutton = view.findViewById(R.id.refreshbutton);
        testbutton2 = view.findViewById(R.id.teestbutton);
        storeDatainArrays();
        recyclerview = view.findViewById(R.id.recycleviewstocks);
        portfoliostockadapter = new portfoliostockrecycleradapter(getActivity(),getActivity(), book_id, book_title, book_author, book_pages);
        recyclerview.setAdapter(portfoliostockadapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        gotofragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewDialog();

            }
        });
        testbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                updatestock();
        }
        });

        testbutton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mysecondDB.addentry("12133.32","10/23/2021");
                mysecondDB.addentry("12133.32","10/24/2021");
                mysecondDB.addentry("14213.53","10/25/2021");
                mysecondDB.addentry("11231.91","10/26/2021");
                mysecondDB.addentry("17130.23","10/27/2021");
                mysecondDB.addentry("11425.12","10/28/2021");
                mysecondDB.addentry("7130.32","10/29/2021");
                mysecondDB.addentry("7130.32","10/30/2021");
                mysecondDB.addentry("7130.32","10/30/2021");
                mysecondDB.addentry("0","11/01/2021");
    }
        });

        return view;
    }


    // Graph code - Ariq
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chartTitleTextView2 = view.findViewById(R.id.chartTitleTextView2);
        portfolioChart = view.findViewById(R.id.lineChart);

        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> balance = new ArrayList<>();
        ArrayList<String> date_entry = new ArrayList<>();

        List<Integer> colors = new ArrayList<>();
        int greeen  = Color.rgb(110,190,102);
        int reed = Color.rgb(211,87,44);

        //chartTitleTextView2.setText();
        storeDataInArraysPortfolio(id, balance, date_entry);

        ArrayList<Entry> portfolioVals = new ArrayList<Entry>();
        ArrayList portfolioDateTimes = new ArrayList();
        System.out.print(date_entry+"HELLO");
        for(int i = 0; i < balance.size(); i++) {
            float xVal = Float.valueOf(balance.get(i));
            portfolioVals.add(new Entry(i, (int) xVal));
            portfolioDateTimes.add(date_entry.get(i));
        }

        LineDataSet portfolioSet = new LineDataSet (portfolioVals, "Porfolio Balance Chart");
        portfolioChart.animateY(0);
        LineData data = new LineData(portfolioDateTimes, portfolioSet);
        //portfolioSet .setColors(colors);
        Legend legend = portfolioChart.getLegend();
        legend.setEnabled(false); // hide legend

        portfolioChart.setDescription("Portfolio Balance Chart");
        portfolioChart.setData(data);
    }

    // From Jimmy's code
    void storeDataInArraysPortfolio(ArrayList<String> id, ArrayList<String> balance, ArrayList<String> date_entry) {
        Cursor cursor = mysecondDB.readAllData();
        if(cursor.getCount() == 0) {
            //Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }else {
            while(cursor.moveToNext()) {
                id.add(cursor.getString(0));
                balance.add(cursor.getString(1));
                date_entry.add(cursor.getString(2));


            }
        }
    }




    //this is for the add stock popup
    public void createNewDialog() {
        dialogbuilder = new AlertDialog.Builder(getActivity()); //the video used this might be an issue
        final View popupview = getLayoutInflater().inflate(R.layout.popupaddstockcrypto, null);
        popup_stockname = (EditText) popupview.findViewById(R.id.popupaddstockcrypto_stock);
        popup_stockamount = (EditText) popupview.findViewById(R.id.popupaddstockcrypto_amount);
        popup_cancelbutton = (Button) popupview.findViewById(R.id.popupaddstockcrypto_cancelbutton);
        popup_savebutton = (Button) popupview.findViewById(R.id.popupaddstockcrypto_savebutton);
        dialogbuilder.setView(popupview);
        dialog = dialogbuilder.create();
        dialog.show();


        popup_cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        popup_savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                String testapi = "https://cloud.iexapis.com/stable/stock/market/batch?symbols=" + popup_stockname.getText().toString().trim() +"&types=quote&range=1m&last=5&token=sk_312389e990ff49af9d13a20cc770ec95";
                client.get(testapi, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONObject results = jsonObject;
                            JSONObject p = results.getJSONObject(toUpperCase(popup_stockname.getText().toString().trim()));
                            p = p.getJSONObject("quote");
                            myDB.addstock(popup_stockname.getText().toString().trim(), p.getString("latestPrice"), Integer.parseInt(popup_stockamount.getText().toString()),"a");
                            tryredraw();

                        } catch (JSONException e) {

                            e.printStackTrace();


                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        System.out.println("Failed");

                    }
                });


                dialog.dismiss();

            }
        });
    }
/*
    void fixsector(String sector){
        AsyncHttpClient client = new AsyncHttpClient();

        String testapi = "https://financialmodelingprep.com/api/v3/profile/" + sector + "?apikey=d610507a84e6b54992411a018867a0b7";
        System.out.println(testapi);
        client.get(testapi, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonObject = json.jsonArray;
                try {
                JSONObject p = jsonObject.getJSONObject(0);
                String pa = p.getString("sector");


                } catch (JSONException e) {
                    System.out.println("JSONEXCEPTION1");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                System.out.println("JSONEXCEPTION2");
            }
        });



    }

*/



    void storeDatainArrays() {

        Cursor cursor = myDB.readAllData();

        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));

            }

        }

    }

    void returnstock(String stocknames,Vector<String> stockss) {
        AsyncHttpClient client = new AsyncHttpClient();
        String testapi = "https://api.twelvedata.com/price?symbol=" + stocknames + "&apikey=f0b21df90101477184b43faf1d393bc9";
        System.out.println(testapi);
        ArrayList<String> testing = new ArrayList<>();
        client.get(testapi, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject results = jsonObject;
                    for(int i=0;i<stockss.size();i++){
                        JSONObject p = results.getJSONObject(stockss.get(i));
                        String symbol = p.getString("price");
                        testing.add(p.getString("price"));
                        System.out.println(testing.size());
                        System.out.println(symbol);
                    }

                tryredraw();
                } catch (JSONException e) {

                    e.printStackTrace();


                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });


    }

    void tryredraw(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerview.setAdapter(null);
                recyclerview.setLayoutManager(null);
                book_id = new ArrayList<>();
                book_author = new ArrayList<>();
                book_title = new ArrayList<>();
                book_pages = new ArrayList<>();
                storeDatainArrays();
                portfoliostockadapter = new portfoliostockrecycleradapter(getActivity(),getActivity(), book_id, book_title, book_author, book_pages);
                recyclerview.setAdapter(portfoliostockadapter);
                recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });

    }

    void updatestock(){
        Cursor cursor = myDB.readAllData();
        String stocknamesb = "";
        ArrayList<String> stonks = new ArrayList<>();
        ArrayList<String> testvector = new ArrayList();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {

                stocknamesb = stocknamesb + cursor.getString(1).toString() + ",";
                stonks.add(cursor.getString(1));
            }
        }
        AsyncHttpClient client = new AsyncHttpClient();
        String testapi = "https://api.twelvedata.com/price?symbol=" + stocknamesb + "&apikey=f0b21df90101477184b43faf1d393bc9";
        System.out.println(testapi);
        client.get(testapi, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONObject results = jsonObject;
                    for(int i=0;i<stonks.size();i++)
                    {

                        JSONObject p = results.getJSONObject(stonks.get(i));

                       testvector.add(p.getString("price"));


                    }
                    System.out.println(testvector.size());
                    myportfoliodatabase testdbthing = new myportfoliodatabase(getActivity());
                    Cursor cursor3 = myDB.readAllData();
                    int testnum = 0;
                    if (cursor3.getCount() == 0) {
                        Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                    } else {
                        while (cursor3.moveToNext()) {
                            testdbthing.updateData(cursor3.getString(0),
                                    cursor3.getString(1),
                                    testvector.get(testnum),
                                    cursor3.getString(3));
                            System.out.println(testvector.get(testnum));
                            testnum = testnum + 1;
                        }
                        tryredraw();
                    }
                } catch (JSONException e) {
                    System.out.println("JSONEXCEPTION1");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                System.out.println("JSONEXCEPTION2");
            }
        });




    }


}


/*
Handler handler = new Handler();
Runnable runnable;
int delay = 15*1000; //Delay for 15 seconds.  One second = 1000 milliseconds.


@Override
protected void onResume() {
   //start handler as activity become visible

    handler.postDelayed( runnable = new Runnable() {
        public void run() {
            //do something
        setstockinfo();
            handler.postDelayed(runnable, delay);
        }
    }, delay);

    super.onResume();
}

// If onPause() is not included the threads will double up when you
// reload the activity

@Override
protected void onPause() {
    handler.removeCallbacks(runnable); //stop handler when activity not visible
    super.onPause();
}
*/