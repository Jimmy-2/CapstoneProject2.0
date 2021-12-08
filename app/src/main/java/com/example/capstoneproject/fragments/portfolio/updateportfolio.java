package com.example.capstoneproject.fragments.portfolio;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.capstoneproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class updateportfolio extends AppCompatActivity {
    EditText title_input, pages_input;
    Button update_button, delete_button;
    String id, title, author, pages;
    int saveintialamount;
    databaseforachievements achievementDB;
    databaseforbalance balanceDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateportfolio);
        title_input = findViewById(R.id.popupaddstockcrypto_stock2);
        pages_input = findViewById(R.id.popupaddstockcrypto_amount2);
        update_button = findViewById(R.id.popupaddstockcrypto_savebutton2);
        delete_button = findViewById(R.id.popupaddstockcrypto_deletebutton);
        balanceDB = new databaseforbalance(updateportfolio.this);
        achievementDB = new databaseforachievements(updateportfolio.this);
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                myportfoliodatabase myDB = new myportfoliodatabase(updateportfolio.this);

                pages = pages_input.getText().toString().trim();
                //saveintialamount = Integer.parseInt(pages) + saveintialamount;
                //pages = String.valueOf(saveintialamount);
                AsyncHttpClient client = new AsyncHttpClient();

                /*String testapi = "https://cloud.iexapis.com/stable/stock/market/batch?symbols=" + popup_stockname.getText().toString().trim() +"&types=quote&range=1m&last=5&token=sk_312389e990ff49af9d13a20cc770ec95";
                 */
                String testapi = "https://financialmodelingprep.com/api/v3/profile/" + title + "?apikey=d610507a84e6b54992411a018867a0b7";
                client.get(testapi, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        JSONArray jsonObject = json.jsonArray;
                        try {
                            JSONObject p = jsonObject.getJSONObject(0);
                            /*
                            JSONObject p = results.getJSONObject(toUpperCase(popup_stockname.getText().toString().trim()));

                            p = p.getJSONObject("quote");
                            */
                            try {
                                if (Integer.parseInt(pages)<0) {
                                    Toast.makeText(updateportfolio.this, "Error!, amount less than 0", Toast.LENGTH_SHORT).show();

                                } else {

                                int x = Integer.parseInt(pages);
                                System.out.println((int) Double.parseDouble(p.getString("price")));
                                x = x * (int) Double.parseDouble(p.getString("price"));
                                x = returnbalance() - x;
                                if (x < 0) {
                                    Toast.makeText(updateportfolio.this, "Error!, not enough money", Toast.LENGTH_SHORT).show();
                                } else {
                                    balanceDB.updateData(String.valueOf(returnbalanceid()), String.valueOf(x));
                                    saveintialamount = Integer.parseInt(pages) + saveintialamount;
                                    pages = String.valueOf(saveintialamount);
                                    myDB.updateData(id, title, author, pages);
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                                    finish();
                                }
                            }
                            } catch (NumberFormatException e) {
                                Toast.makeText(updateportfolio.this, "Error!, not an integer on stock amount", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                            Toast.makeText(updateportfolio.this, "Error!, please check stock name", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        System.out.println("Failed");
                        Toast.makeText(updateportfolio.this, "Error!, please check stock name", Toast.LENGTH_SHORT).show();

                    }
                });
                /*
                myDB.updateData(id, title, author, pages);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                finish();

                 */


            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                myportfoliodatabase myDB = new myportfoliodatabase(updateportfolio.this);

                pages = pages_input.getText().toString().trim();
                //saveintialamount = Integer.parseInt(pages) + saveintialamount;
                //pages = String.valueOf(saveintialamount);
                AsyncHttpClient client = new AsyncHttpClient();

                /*String testapi = "https://cloud.iexapis.com/stable/stock/market/batch?symbols=" + popup_stockname.getText().toString().trim() +"&types=quote&range=1m&last=5&token=sk_312389e990ff49af9d13a20cc770ec95";
                 */
                String testapi = "https://financialmodelingprep.com/api/v3/profile/" + title + "?apikey=d610507a84e6b54992411a018867a0b7";
                client.get(testapi, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        JSONArray jsonObject = json.jsonArray;
                        try {
                            JSONObject p = jsonObject.getJSONObject(0);
                            /*
                            JSONObject p = results.getJSONObject(toUpperCase(popup_stockname.getText().toString().trim()));

                            p = p.getJSONObject("quote");
                            */
                            try {
                                int x = Integer.parseInt(pages);
                                System.out.println((int) Double.parseDouble(p.getString("price")));
                                x = x * (int) Double.parseDouble(p.getString("price"));
                                x = returnbalance() + x;
                                if (Integer.parseInt(pages) < 0)
                                {
                                    Toast.makeText(updateportfolio.this, "Error!, amount less than 0", Toast.LENGTH_SHORT).show();

                                } else {

                                if (Integer.parseInt(pages) > saveintialamount) {
                                    Toast.makeText(updateportfolio.this, "Error!, not enough stocks", Toast.LENGTH_SHORT).show();
                                } else {
                                    balanceDB.updateData(String.valueOf(returnbalanceid()), String.valueOf(x));
                                    saveintialamount = saveintialamount - Integer.parseInt(pages);
                                    pages = String.valueOf(saveintialamount);
                                    checkachievements(x);
                                    if (saveintialamount == 0) {
                                        myDB.deleteOneRow(id);
                                        finish();
                                    } else {
                                        myDB.updateData(id, title, author, pages);
                                        finish();
                                    }

                                }
                            }
                            } catch (NumberFormatException e) {
                                Toast.makeText(updateportfolio.this, "Error!, not an integer on stock amount", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                            Toast.makeText(updateportfolio.this, "Error!, please check stock name", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        System.out.println("Failed");
                        Toast.makeText(updateportfolio.this, "Error!, please check stock name", Toast.LENGTH_SHORT).show();

                    }
                });
                /*
                myDB.updateData(id, title, author, pages);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                finish();

                 */


            }
            /*
            public void onClick(View view) {
                myportfoliodatabase myDB = new myportfoliodatabase(updateportfolio.this);
                myDB.deleteOneRow(id);
                finish();
            }

             */
        });

    }

    void checkachievements(int value){
        Cursor cursor = achievementDB.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(updateportfolio.this, "No achievements, please set balance", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                System.out.println(Integer.valueOf(cursor.getString(2)));
                if(value >= Integer.valueOf(cursor.getString(2))){
                    if(Integer.valueOf(cursor.getString(0))>5){
                        if(value <= Integer.valueOf(cursor.getString(2))){
                            achievementDB.updateData2(cursor.getString(0),cursor.getString(1),cursor.getString(2),"true");

                        }
                    }
                    else{
                        achievementDB.updateData2(cursor.getString(0),cursor.getString(1),cursor.getString(2),"true");
                    }

                }
            }
            }
    }

    int returnbalance(){
        Cursor cursor2 = balanceDB.readAllData();
        int bal = 0;
        if(cursor2.getCount()==0){
            return 0;
        }
        else {
            while (cursor2.moveToNext()) {
                bal = Integer.parseInt(cursor2.getString(1));
            }
        }
        return bal;
    }
    int returnbalanceid(){
        Cursor cursor2 = balanceDB.readAllData();
        int bal = 0;
        if(cursor2.getCount()==0){
            return 0;
        }
        else {
            while (cursor2.moveToNext()) {
                bal = Integer.parseInt(cursor2.getString(0));
            }
        }
        return bal;
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("author") && getIntent().hasExtra("pages")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            pages = getIntent().getStringExtra("pages");
            saveintialamount = Integer.parseInt(getIntent().getStringExtra("pages"));
            //Setting Intent Data
            title_input.setText(title);

            pages_input.setText(pages);
            //Log.d("stev", title + " " + author + " " + pages);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + " ?");
        builder.setMessage("Are you sure you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //myDB.deleteOneRow(id);
                finish();
            }
        });
    }



}