package com.example.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.capstoneproject.models.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    Context context;
    List<Article> articles;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }


    @NonNull
    @Override
    // change orientation. change view name
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);

        //get data and populate the view holder with data of the movie
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvText;
        ImageView ivThumbnail;
        ImageView ivSentIm;
        TextView tvTickers;
        //TextView tvSentiment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvText = itemView.findViewById(R.id.tvText);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            ivSentIm = itemView.findViewById(R.id.ivSentIm);
            container = itemView.findViewById(R.id.container);
            tvTickers = itemView.findViewById(R.id.tvTickers);
           // tvSentiment = itemView.findViewById(R.id.tvSentiment);
        }

        public void bind(Article article) {
            tvTitle.setText(article.getTitle());
            tvText.setText(article.getText());
            //tvSentiment.setText(article.getSentiment());
            tvTickers.setText(article.getTickers().replace("[","").replace("]","").replace("\"",""));
            Glide.with(context).load(article.getPosterPath()).into(ivThumbnail);

            if(article.getSentiment().equals("Positive")){
                Glide.with(context).load("https://www.pngkit.com/png/detail/99-990198_thumbs-up-icon-thumbs-up-green-icon.png").into(ivSentIm);
            }
            else if(article.getSentiment().equals("Negative")){
                Glide.with(context).load("https://toppng.com//public/uploads/preview/thumb-signal-red-clip-art-thumbs-down-red-11563640133vddccjjqo5.png").into(ivSentIm);
            }
            else{
                Glide.with(context).load("https://cdn.pixabay.com/photo/2017/06/13/12/51/thumb-2398754_960_720.png").into(ivSentIm);
            }


            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getLink()));
                    context.startActivity(intent);
                }
            });
        }
    }
}

