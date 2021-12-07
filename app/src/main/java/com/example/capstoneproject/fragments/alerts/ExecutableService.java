/**
 * Created by Jimmy.
 * */

package com.example.capstoneproject.fragments.alerts;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.capstoneproject.MainActivity;
import com.example.capstoneproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

import okhttp3.Headers;


public class ExecutableService extends BroadcastReceiver {

    final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
    AlertsDatabaseHelper alertDB;
    ArrayList<String> alert_id, symbol, name, currentPrice, alertPrice;
    Hashtable<String, String> alertHashTable = new Hashtable<String, String>();
    String batchTicker = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("HELLO IT IS WORKING!");

        alertDB = new AlertsDatabaseHelper(context);
        alert_id = new ArrayList<>();
        symbol = new ArrayList<>();
        name = new ArrayList<>();
        currentPrice = new ArrayList<>();
        alertPrice = new ArrayList<>();


        Cursor cursor = alertDB.readAllDataSorted("_id", "Asc");
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

        for (int i = 0; i < alert_id.size(); i++) {

            System.out.println("HELLO"+alert_id.get(i)+symbol.get(i)+String.valueOf(alert_id.get(i)));
        }

        for (int i = 0; i < alertDB.getAlertsCount(); i++) {
            alertHashTable.put(String.valueOf(symbol.get(i)), "0");

            //add each stock ticker from the database into a hashset
            //tickerSet.add(String.valueOf(symbol.get(i)));
        }
        Set<String> keys = alertHashTable.keySet();
        for (String key : keys) {
            batchTicker = batchTicker.concat(key + ",");
        }
        //System.out.println(batchTicker);
        //batchTicker = TextUtils.join(",",tickerSet);


        //api link to get NEW batch stock prices
        String TEST_API = "https://api.twelvedata.com/price?symbol=" + batchTicker + "&apikey=c2c894e47847490993e8704e2fe75dd6";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TEST_API, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                String testing = "";
                try {
                    for (String key : keys) {
                        JSONObject results = jsonObject.getJSONObject(key);
                        String stockPrice = results.getString("price");
                        alertHashTable.put(key, stockPrice);
                        System.out.println(key + stockPrice);
                    }

                    System.out.println(alertHashTable.toString());

                    //count each database item (recyclerview item count) and update it's currentPrice with the new one from the api call. Increment through its position
                    for (int i = 0; i < alertDB.getAlertsCount(); i++) {
                        Boolean alertHighOrLow;
                        if(Double.parseDouble(alertPrice.get(i)) >= Double.parseDouble(currentPrice.get(i))) {
                            alertHighOrLow = true;
                        }else {
                            alertHighOrLow= false;
                        }

                        double newVal = Double.parseDouble(alertHashTable.get(symbol.get(i)));
                        System.out.println(newVal+"HELLO");
                        // create and runs the notifications

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Noti", NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager manager = context.getSystemService(NotificationManager.class);
                            manager.createNotificationChannel(channel);


                        }
                        Random random = new Random();

                        int ran = random.nextInt(9999 - 1000) + 1000;
                        if(alertHighOrLow == true) {
                            if (newVal > Double.parseDouble(alertPrice.get(i)) ) {
                                //using random numbers for notification ids so that there can be more than 1 notification in the notification panel otherwise new notifications will replace old ones

                                Notification.Builder notification = new Notification.Builder
                                        (context, CHANNEL_ID).setContentTitle("PRICE ALERT").setContentText(symbol.get(i)+" has reached higher than your alert price of " + alertPrice.get(i)).setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true);
                                NotificationManagerCompat.from(context).notify(ran,notification.build());

                                alertDB.deleteRow(String.valueOf(alert_id.get(i)));
                                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                AlertsCompletedDatabaseHelper alertCompletedDB = new AlertsCompletedDatabaseHelper(context);
                                alertCompletedDB.addAlertCompleted(symbol.get(i),
                                        name.get(i),
                                        currentDateTimeString ,
                                        Double.valueOf(alertPrice.get(i)));
                            }
                        }else if (alertHighOrLow == false) {
                            if (newVal < Double.parseDouble(alertPrice.get(i)) ) {
                                //using random numbers for notification ids so that there can be more than 1 notification in the notification panel otherwise new notifications will replace old ones

                                Notification.Builder notification = new Notification.Builder
                                        (context, CHANNEL_ID).setContentTitle("PRICE ALERT").setContentText(symbol.get(i)+" has reached lower than your alert price of " + alertPrice.get(i)).setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true);
                                NotificationManagerCompat.from(context).notify(ran,notification.build());
                                alertDB.deleteRow(String.valueOf(alert_id.get(i)));
                                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                AlertsCompletedDatabaseHelper alertCompletedDB = new AlertsCompletedDatabaseHelper(context);
                                alertCompletedDB.addAlertCompleted(symbol.get(i),
                                        name.get(i),
                                        currentDateTimeString ,
                                        Double.valueOf(alertPrice.get(i)));
                            }
                        }


                        alertDB.updatePriceDatabase(String.valueOf(alert_id.get(i)), alertHashTable.get(symbol.get(i)));
                    }


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
                    e.printStackTrace();


                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });










    }

    public void getNewCurrentPrice() {

    }


}