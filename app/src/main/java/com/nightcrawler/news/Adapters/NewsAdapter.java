package com.nightcrawler.news.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nightcrawler.news.DataObjects.Articles;
import com.nightcrawler.news.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private Articles articles;
    private final LayoutInflater inflater;
    private Context context;

    public NewsAdapter(Context context, Articles articles) {
        Log.d("TEST","In NewsAdapter");
        Log.d("TEST", String.valueOf(context!=null));
        this.context = context;
        this.articles = articles;
        if(this.articles!=null)
            Log.d("TEST","Articles not null in NewsAdapter");
        inflater = LayoutInflater.from(context);
        Log.d("TEST", String.valueOf(inflater!=null));
    }

public void setDataSource(Articles articles)
{
    this.articles=articles;
    notifyDataSetChanged();
}



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.article_description.setText(articles.getArticles().get(position).getTitle());
        Glide.with(holder.card_iv.getContext()).load(articles.getArticles().get(position).getUrlToImage()).into(holder.card_iv);

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView article_description;
        private ImageView card_iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            card_iv = itemView.findViewById(R.id.card_iv);
            article_description = itemView.findViewById(R.id.article_description);
        }

        @Override
        public void onClick(View view) {

        }
    }


}
