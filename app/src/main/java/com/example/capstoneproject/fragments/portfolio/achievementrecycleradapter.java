package com.example.capstoneproject.fragments.portfolio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.fragments.chartsgraphs.StockGraphFragment;

import java.util.ArrayList;

public class achievementrecycleradapter extends RecyclerView.Adapter<achievementrecycleradapter.MyViewHolder>{

    private final Context context;
    private final ArrayList completed;
    private final ArrayList achievement;
    private final String p = "true";

    private Button button;
    private Activity activity;
    int position;
    achievementrecycleradapter(Activity activity, Context context, ArrayList completed, ArrayList achievement){
        this.activity = activity;
        this.context = context;
        this.completed = completed;
        this.achievement = achievement;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.achievement_recycler_resource,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.position = position;
        holder.achievement.setText(String.valueOf(achievement.get(position)));
        System.out.println(String.valueOf(completed.get(position)).trim());
        if(completed.get(position).equals("true")) {
            System.out.println("success");
            holder.mainLayout.setBackgroundColor(Color.parseColor("#00FF00"));

        }
        }

    @Override
    public int getItemCount(){
        return achievement.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView achievement;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            achievement = itemView.findViewById(R.id.achievementtext);
            mainLayout = itemView.findViewById(R.id.portfoliorecyclerlayout);
        }
    }
    }









