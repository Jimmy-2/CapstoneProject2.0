package com.example.capstoneproject.fragments.portfolio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.AlertsAdapter;
import com.example.capstoneproject.R;
import com.example.capstoneproject.fragments.StockGraphFragment;

import java.util.ArrayList;
import java.util.List;

public class portfoliostockrecycleradapter extends RecyclerView.Adapter<portfoliostockrecycleradapter.MyViewHolder>{

    private final Context context;
    private final ArrayList book_id;
    private final ArrayList book_title;
    private final ArrayList book_author;
    private final ArrayList book_pages;

    private Button button;
    private Activity activity;
    int position;
    portfoliostockrecycleradapter(Activity activity,Context context,ArrayList book_id, ArrayList book_title, ArrayList book_author, ArrayList book_pages){
        this.activity = activity;
        this.context = context;
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_pages = book_pages;
        //portfolioobject = new portfolio();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.portfolio_recycler_resource,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.position = position;

        /*
        holder.book_id_txt.setText(String.valueOf(book_id.get(position)));
        holder.book_title_txt.setText(String.valueOf(book_title.get(position)));
        holder.book_author_txt.setText(String.valueOf(book_author.get(position)));
        holder.book_pages_txt.setText(String.valueOf(book_pages.get(position)));

         */
        holder.book_id_txt.setText(String.valueOf(book_title.get(position)));
        holder.book_title_txt.setText(String.valueOf(book_author.get(position)));
        holder.book_author_txt.setText(String.valueOf(book_pages.get(position)));
        double totalamount = Integer.valueOf(String.valueOf(book_pages.get(position))) * Double.parseDouble(String.valueOf(book_author.get(position)));
        holder.book_pages_txt.setText(String.valueOf(totalamount));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to ariq's graph idk how to yet
                //System.out.println("test");
               // Fragment fragment;
                //fragment = new StockGraphFragment();


                //from AlertsAdapter - start
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                StockGraphFragment fragment = new StockGraphFragment();
                //using bundle to pass data to another fragment
                Bundle args = new Bundle();
                //key, value
                args.putString("stockTicker", String.valueOf(book_title.get(position)));
                args.putString("graphTime", "5min");
                System.out.println(book_title.get(position)+"HELLO");
                fragment.setArguments(args);
                //add a stack so we can click back button to go back
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();
                // - end
            }


        });
    }


    @Override
    public int getItemCount(){
        return book_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
        Button button;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            book_id_txt = itemView.findViewById(R.id.stockinfotextview);
            book_title_txt = itemView.findViewById(R.id.stockinfotextviewprice);
            book_author_txt = itemView.findViewById(R.id.portfoliorecyclerstockamount);
            book_pages_txt = itemView.findViewById(R.id.textView7);
            button = itemView.findViewById(R.id.deletebutton);



            mainLayout = itemView.findViewById(R.id.portfoliorecyclerlayout);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, updateportfolio.class);
                    intent.putExtra("id", String.valueOf(book_id.get(position)));
                    intent.putExtra("title", String.valueOf(book_title.get(position)));
                    intent.putExtra("author", String.valueOf(book_author.get(position)));
                    intent.putExtra("pages", String.valueOf(book_pages.get(position)));
                    activity.startActivityForResult(intent, 1);
                }
            });




        }

    }

    /*
    private ArrayList<portfoliostock> portfoliostocklist;

    private Context context;
    private ArrayList book_id,book_title,book_author,book_pages;
    public portfoliostockrecycleradapter(Context context,ArrayList book_id,ArrayList book_title,ArrayList book_author, ArrayList book_pages){
        this.context = context;
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_pages = book_pages;
    }

            ArrayList<portfoliostock> portfoliostocklist){
        this.portfoliostocklist = portfoliostocklist;

    }
 /*
    @NonNull
    public MyViewHolder OnCreateViewHolder (@NonNull ViewGroup parent ,int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.portfolio_recycler_resource,parent, false);
        return new MyViewHolder(view);
    }

     */
/*
    public class MyViewHolder extends RecyclerView.ViewHolder{


          private TextView nametext;
        private TextView priceText;
        private TextView amountText;
        public MyViewHolder(final View view){
            super(view);
            nametext = view.findViewById(R.id.stockinfotextview);
            priceText = view.findViewById(R.id.stockinfotextviewprice);
            amountText = view.findViewById(R.id.portfoliorecyclerstockamount);
        }
    }

    @NonNull
    @Override
    public portfoliostockrecycleradapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_recycler_resource,parent,false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull portfoliostockrecycleradapter.MyViewHolder holder, int position) {
        String name = portfoliostocklist.get(position).getStockname();
        String price = portfoliostocklist.get(position).getStockPrice();
        int amount = portfoliostocklist.get(position).getStockamount();
        holder.nametext.setText(name);
        holder.priceText.setText(price);
        holder.amountText.setText(String.valueOf(amount));

    }

    @Override
    public int getItemCount() {
        return portfoliostocklist.size();
    } */
}

