package com.example.capstoneproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.capstoneproject.ArticleAdapter;
import com.example.capstoneproject.R;
import com.example.capstoneproject.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    public static final String TAG = "MainActivity";
    EditText tvSearch;
    RecyclerView rvArticles;
    Button btnSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    List<Article> articles;
    ArticleAdapter articleAdapter;
    //AsyncHttpClient client;
    String url ;
    int numItems = 20;
    String tickers;

    private RequestQueue requestQueue;

    SharedPreferences sharedPreferences;

    public NewsFragment() {
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
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSearch = (EditText) view.findViewById(R.id.tvSearch);
        rvArticles = view.findViewById(R.id.rvArticles);
        btnSearch = view.findViewById(R.id.btnSearch);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this.getContext(), articles);

        rvArticles.setAdapter(articleAdapter);
        rvArticles.setLayoutManager(new LinearLayoutManager(this.getContext()));

        requestQueue = Volley.newRequestQueue(this.getContext());

        sharedPreferences = getActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        //Toast.makeText(getContext(), sharedPreferences.getString("url", ""), Toast.LENGTH_SHORT).show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, sharedPreferences.getString("url", "") , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            articles.clear();
                            JSONArray results = response.getJSONArray("data");// results is an array in the json
                            articles.addAll(Article.fromJsonArray(results));
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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse("");

            }
        });

        tvSearch.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i , KeyEvent keyEvent){
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == 66)) {
                    jsonParse("");
                    return true;
                }

                return false;
            }
        });

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateOperation();
                    }
                }
        );
    }

    private void jsonParse(String defURL){
        tickers = tvSearch.getText().toString();

        // if tickers is empty, the user must have left the fragment or done something to reinvoke onCreateView()
        if(tickers == ""){
            // in this case use the saved url from their previous interaction with this screen
            Toast.makeText(getContext(), "Empty tickere", Toast.LENGTH_SHORT).show();
            url = defURL;
        }else{
            // else the user must have entered a new set of tickers. save the new url in sharedPreferences
            url = String.format("https://stocknewsapi.com/api/v1?tickers=%s,&items=%d&token=i0rpdgcnbrcgaimxbclxhztmuu6sk8jm79zcludj&fbclid=IwAR0pguARasu-pDs_Jcy4Wc4fCL_JIXCjRc_JYwsSN57xOSCnhleL3I2LDHA",tickers,numItems);
            // save url in shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("url", url);
            editor.commit();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            articles.clear();
                            JSONArray results = response.getJSONArray("data");// results is an array in the json
                            articles.addAll(Article.fromJsonArray(results));
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
        jsonParse("");
        swipeRefreshLayout.setRefreshing(false);
    }




}