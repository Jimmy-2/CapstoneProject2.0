package com.example.capstoneproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.capstoneproject.R;
import com.google.android.material.textfield.TextInputLayout;

public class AdvancedOptionsFragment extends Fragment {
    SharedPreferences sharedPreferences;

    TextInputLayout mnSort;
    EditText tvItemCount;
    EditText tvExclude;
    SeekBar sbSentiment;
    SeekBar sbType;
    Button btnSearch;

    String itemCount;

    public AdvancedOptionsFragment() {
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
        return inflater.inflate(R.layout.fragment_advanced_options, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvItemCount = (EditText)view.findViewById(R.id.tvItemCount);
        tvExclude = (EditText)view.findViewById(R.id.tvExclude);
        sbSentiment = view.findViewById(R.id.sbSentiment);
        sbType = view.findViewById(R.id.sbType);
        mnSort = view.findViewById(R.id.mnSort);
        btnSearch = view.findViewById(R.id.btnSearchAdv);

        // sharedPreferences will be used to save user queries.
        sharedPreferences = getActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        //itemCount = tvItemCount.getText().toString();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save numItems in shared preferences.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("itemCount", tvItemCount.getText().toString());
                editor.commit();

                editor.putString("excludeSource", tvExclude.getText().toString());
                editor.commit();

                editor.putString("sentimentFilter", Integer.toString(sbSentiment.getProgress()));
                editor.commit();

                editor.putString("typeFilter", Integer.toString(sbType.getProgress()));
                editor.commit();

                //String seekBarValue = Integer.toString(sbSentiment.getProgress());
                // go back to news screen with item count saved
                //FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //transaction.replace(R.id.flContainer, new NewsFragment() ); // give your fragment container id in first parameter
                //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                //transaction.commit();
                Toast.makeText(getActivity(),sharedPreferences.getString("typeFilter", "") ,Toast.LENGTH_SHORT).show();
            }
        });

    }
}