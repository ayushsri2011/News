
package com.nightcrawler.news.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nightcrawler.news.Activities.ArticleReadActivity;
//import com.nightcrawler.news.Analytics.AnalyticsApplication;
import com.nightcrawler.news.DataObjects.Article;
import com.nightcrawler.news.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private Context context;
    private List<Article> articles;

    public NewsAdapter(Context context) {
        this.context = context;

    }

    public void setDataSource(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_layout, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.article_description.setText(articles.get(position).getTitle());
        holder.article_author.setText(articles.get(position).getAuthor());
        try {
            if (!articles.get(position).getUrlToImage().equals("") && !Objects.equals(articles.get(position).getUrlToImage(), " "))
                Picasso.get()
                        .load(articles.get(position).getUrlToImage())
                        .placeholder(R.drawable.news).into(holder.card_iv);
        } catch (Exception e) {
            holder.card_iv.setImageResource(R.drawable.news);
        }


    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView article_description;
        private ImageView card_iv;
        private TextView article_author;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            card_iv = itemView.findViewById(R.id.card_iv);
            article_description = itemView.findViewById(R.id.article_description);
            article_author = itemView.findViewById(R.id.article_author);
        }

        @Override
        public void onClick(View view) {

            int pos = getAdapterPosition();
            Intent intent = new Intent(context, ArticleReadActivity.class);
            intent.putExtra("url", articles.get(pos).getUrl());
            intent.putExtra("author", articles.get(pos).getAuthor());
            intent.putExtra("urlToImage", articles.get(pos).getUrlToImage());
            intent.putExtra("title", articles.get(pos).getTitle());
            intent.putExtra("publishedAt", articles.get(pos).getPublishedAt());
            context.startActivity(intent);
        }
    }


}