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

//        ArrayAdapter ;

        // set up a queue for get requests.
        requestQueue = Volley.newRequestQueue(this.getContext());

        // sharedPreferences will be used to save user queries.
        sharedPreferences = getActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, new AdvancedOptionsFragment() ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();

    }

    private void jsonParse(){
        // if tickers text field is empty
        if(tvSearch.getText().toString() == ""){
            // try to get tickers from last search.
            //tickers = sharedPreferences.getString(url, "");
            tickers = "";
        }
        else{
            // else get tickers from text field.
            tickers = tvSearch.getText().toString();
        }

        // if item count text field is empty
        //if(tvItemCount.getText().toString() == ""){
        // try to get item count from last search.
        //numItems = sharedPreferences.getString(url, "");
        //  numItems = "10";
        //}
        //else{
        // else get item count from text field.
        //  numItems = tvItemCount.getText().toString();
        //}

        // create url for get request.
        Toast.makeText(getActivity(),sharedPreferences.getString("excludeSource", ""),Toast.LENGTH_SHORT).show();

        url = String.format("https://stocknewsapi.com/api/v1?tickers=%s&items=%s&sourceexclude=%s&token=i0rpdgcnbrcgaimxbclxhztmuu6sk8jm79zcludj&fbclid=IwAR0pguARasu-pDs_Jcy4Wc4fCL_JIXCjRc_JYwsSN57xOSCnhleL3I2LDHA",tickers,sharedPreferences.getString("itemCount", ""),sharedPreferences.getString("excludeSource", ""));

        // save url in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("url", url);
        editor.commit();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, this.url, null,
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