package com.example.capstoneproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.capstoneproject.R;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;

public class AdvancedOptionsFragment extends Fragment {
    SharedPreferences sharedPreferences;

    TextInputLayout mnSort;
    EditText tvItemCount;
    EditText tvExclude;
    SeekBar sbSentiment;
    SeekBar sbType;
    Button btnSearch;
    Button btnReset;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String[] items = {"Newest First", "Oldest First", "Rank"};
    String item;

    public AdvancedOptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Issac");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("Issac2");

        return inflater.inflate(R.layout.fragment_advanced_options, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvItemCount = (EditText)view.findViewById(R.id.tvItemCount);
        tvExclude = (EditText)view.findViewById(R.id.tvExclude);
        sbSentiment = view.findViewById(R.id.sbSentiment);
        sbType = view.findViewById(R.id.sbType);
        btnSearch = view.findViewById(R.id.btnSearchAdv);
        btnReset = view.findViewById(R.id.btnReset);

        // set up sort drop down menu
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
            }
        });

        // sharedPreferences will be used to save user queries.
        sharedPreferences = getActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();



        try{
            tvExclude.setText(sharedPreferences.getString("excludeSource",""));
        }catch(Exception e){
            tvExclude.setText("");
            System.out.println("First launch of app. Shared preferences doesn't exist");
        }
        try{
            tvItemCount.setText(sharedPreferences.getString("itemCount",""));
        }catch(Exception e){
            tvItemCount.setText("10");
        }
        try{
            sbType.setProgress(Integer.parseInt(sharedPreferences.getString("typeFilter","")));
        }catch(Exception e){
            sbType.setProgress(1);
        }
        try{
            sbSentiment.setProgress(Integer.parseInt(sharedPreferences.getString("sentimentFilter","")));
        }catch(Exception e){
            sbSentiment.setProgress(1);
        }
        System.out.println("Issac5");



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save numItems in shared preferences.
                editor.putString("itemCount", tvItemCount.getText().toString());
                editor.commit();

                editor.putString("excludeSource", tvExclude.getText().toString());
                editor.commit();

                editor.putString("sentimentFilter", Integer.toString(sbSentiment.getProgress()));
                editor.commit();

                editor.putString("typeFilter", Integer.toString(sbType.getProgress()));
                editor.commit();

                editor.putString("sort", item);
                editor.commit();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flContainer, new NewsFragment() );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("itemCount", "10");
                tvItemCount.setText("10");
                editor.commit();

                editor.putString("excludeSource", "");
                tvExclude.setText("");
                editor.commit();

                editor.putString("sentimentFilter", "1");
                sbSentiment.setProgress(1);
                editor.commit();

                editor.putString("typeFilter", "1");
                sbType.setProgress(1);
                editor.commit();

                editor.putString("sort", "");
                autoCompleteTextView.setText("Newest First");
                editor.commit();
            }
        });
    }
}