package com.example.capstoneproject.fragments.alerts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.fragments.chartsgraphs.StockGraphFragment;

import java.util.ArrayList;

public class AlertsCompletedAdapter extends RecyclerView.Adapter<AlertsCompletedAdapter.MyViewHolder> {

    Context context;
    ArrayList alert_id, symbol, name, currentPrice, alertPrice;




    public AlertsCompletedAdapter(Context context, ArrayList alert_id, ArrayList symbol, ArrayList name, ArrayList currentPrice, ArrayList alertPrice) {
        this.context = context;
        this.alert_id = alert_id;
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.alertPrice = alertPrice;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alert_completed_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        //holder.tvAlertIdRC.setText(String.valueOf(alert_id.get(position)));
        holder.tvSymbolRC.setText(String.valueOf(symbol.get(position)));
        holder.tvNameRC.setText(String.valueOf(name.get(position)));
        holder.tvCurrentPriceRC.setText(String.valueOf(currentPrice.get(position)));
        holder.tvAlertPriceRC.setText(String.valueOf(alertPrice.get(position)));

        holder.alertCompletedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                StockGraphFragment fragment = new StockGraphFragment();
                //using bundle to pass data to another fragment
                Bundle args = new Bundle();
                //key, value
                args.putString("stockTicker", String.valueOf(symbol.get(position)));
                args.putString("graphTime", "5min");
                System.out.println(symbol.get(position)+"HELLO");
                fragment.setArguments(args);
                //add a stack so we can click back button to go back
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return alert_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvSymbolRC, tvNameRC, tvCurrentPriceRC, tvAlertPriceRC;
        LinearLayout alertCompletedLayout;

        public ImageView ivDeleteAlert;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //tvAlertIdRC = itemView.findViewById(R.id.tvAlertIdRC);
            tvSymbolRC = itemView.findViewById(R.id.tvSymbolRC);
            tvNameRC = itemView.findViewById(R.id.tvNameRC);
            tvCurrentPriceRC = itemView.findViewById(R.id.tvCurrentPriceRC);
            tvAlertPriceRC = itemView.findViewById(R.id.tvAlertPriceRC);

            alertCompletedLayout = itemView.findViewById(R.id.alertCompletedLayout);

            ivDeleteAlert = itemView.findViewById(R.id.ivDeleteAlert);

            ivDeleteAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        //testing position
                        //Toast.makeText(context, "delete"+position,  Toast.LENGTH_SHORT).show();


                        AlertsDatabaseHelper alertDB = new AlertsDatabaseHelper(ivDeleteAlert.getContext());
                        alertDB.deleteRow(String.valueOf(alert_id.get(position)));

                    }

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    AlertsFragment fragment = new AlertsFragment();



                    //refresh the fragment when you delete a recyclerview/database item
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();

                }
            });
        }
    }
}
