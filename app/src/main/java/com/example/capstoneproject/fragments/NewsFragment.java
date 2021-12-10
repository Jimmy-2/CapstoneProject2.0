package com.example.capstoneproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codepath.asynchttpclient.AsyncHttpClient;
//import com.example.capstoneproject.AlertsAdapter;
//import com.example.capstoneproject.AlertsDatabaseHelper;
import com.example.capstoneproject.ArticleAdapter;
import com.example.capstoneproject.R;
import com.example.capstoneproject.fragments.portfolio.portfolio;
import com.example.capstoneproject.models.Article;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    public static final String TAG = "MainActivity";
    private RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvArticles;
    EditText tvSearch;
    Button btnSearch;
    Button btnAdvanced;
    ArticleAdapter articleAdapter;
    List<Article> articles;
    //String numItems;
    String tickers;
    String url ;
    String sentiment;
    String type;
    String itemCount;
    String exclude;
    String sort;

    public NewsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        tvSearch = (EditText) view.findViewById(R.id.tvSearch);
        rvArticles = view.findViewById(R.id.rvArticles);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnAdvanced = view.findViewById(R.id.btnAdvanced);

        // set up array adapter for recyclerview.
        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this.getContext(), articles);
        rvArticles.setAdapter(articleAdapter);
        rvArticles.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // set up a queue for get requests.
        requestQueue = Volley.newRequestQueue(this.getContext());

        // sharedPreferences will be used to save user queries.
        sharedPreferences = getActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        try{
            tvSearch.setText(sharedPreferences.getString("tickers",""));
        }catch(Exception e){
            tvSearch.setText("");
        }

        // populate recyclerview using url saved from previous search.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, sharedPreferences.getString("url", "") , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // clear the articles array of previous searches.
                            articles.clear();

                            // get data.
                            JSONArray results = response.getJSONArray("data");

                            // fill array with data.
                            articles.addAll(Article.fromJsonArray(results));

                            // notify data change.
                            articleAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // add the request queue.
        requestQueue.add(request);

        // make a new get request when user hits enter key while in ticker search field.
        tvSearch.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i , KeyEvent keyEvent){
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == 66)) {
                    // call function that makes get request.
                    jsonParse();
                    return true;
                }
                return false;
            }
        });

        // make a new get request when user click search button.
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse();

            }
        });

        btnAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdvancedOptions();
            }
        });

        // Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user performs swipe downward
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        updateOperation();
                    }
                }
        );
    }

    private void openAdvancedOptions(){
        // save tickers
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tickers", tickers);
        editor.commit();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, new AdvancedOptionsFragment() ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();

    }

    private void jsonParse(){
        // if tickers text field is empty, make a default search. set url to general market news.
        if(tvSearch.getText().toString().equals("")){
            url = "https://stocknewsapi.com/api/v1/category?section=general&items=50&token=i0rpdgcnbrcgaimxbclxhztmuu6sk8jm79zcludj";
        }
        else{
            // else create new url for request.

            // set tickers.
            tickers = tvSearch.getText().toString();

            // set item count.
            itemCount = sharedPreferences.getString("itemCount", "");
            if(itemCount.equals("")){
                itemCount = "10";
            }

            // set exclude source.
            exclude = sharedPreferences.getString("","");
            if(exclude!=""){
                exclude = "&sourceexclude=" + exclude;
            }

            // set sentiment.
            switch(sharedPreferences.getString("sentimentFilter", "")){
                case "0":
                    sentiment = "&sentiment=negative";
                    break;
                case "2":
                    sentiment = "&sentiment=positive";
                    break;
                default:
                    sentiment = "";
            }

            // set type.
            switch(sharedPreferences.getString("typeFilter", "")){
                case "0":
                    type = "&type=video";
                    break;
                case "2":
                    type = "&type=article";
                    break;
                default:
                    type = "";
            }

            // set sort.
            switch(sharedPreferences.getString("sort", "")){
                case "Oldest First":
                    sort = "&sortby=oldestfirst";
                    break;
                case "Rank":
                    sort = "&sortby=rank";
                    break;
                default:
                    sort = "";
            }

            // example url with all user functionality.
            //https://stocknewsapi.com/api/v1?tickers=FB&items=5&sourceexclude=CNBC&type=video&sentiment=positive&sortby=oldestfirst&token=i0rpdgcnbrcgaimxbclxhztmuu6sk8jm79zcludj&fbclid=IwAR0pguARasu-pDs_Jcy4Wc4fCL_JIXCjRc_JYwsSN57xOSCnhleL3I2LDHA";
            url = String.format("https://stocknewsapi.com/api/v1?tickers=%s&items=%s%s%s%s%s&token=i0rpdgcnbrcgaimxbclxhztmuu6sk8jm79zcludj&fbclid=IwAR0pguARasu-pDs_Jcy4Wc4fCL_JIXCjRc_JYwsSN57xOSCnhleL3I2LDHA", tickers, itemCount, sentiment,type,exclude, sort);
        }

        Toast.makeText(getActivity(),sharedPreferences.getString("sort",""),Toast.LENGTH_LONG).show();

        // save url in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("url", url);
        editor.commit();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // clear the articles array of previous searches.
                            articles.clear();

                            // get data.
                            JSONArray results = response.getJSONArray("data");

                            // fill array with data.
                            articles.addAll(Article.fromJsonArray(results));

                            // notify data change.
                            articleAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    private void updateOperation(){
        jsonParse();
        swipeRefreshLayout.setRefreshing(false);
    }




}