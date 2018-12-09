package com.nightcrawler.news.Fragments;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nightcrawler.news.Adapters.NewsAdapter;
import com.nightcrawler.news.DataObjects.Article;
import com.nightcrawler.news.Database.NewsContract;
import com.nightcrawler.news.R;
import com.nightcrawler.news.Utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class LatestNewsFragment extends Fragment {
    private RecyclerView rv;
    private ImageButton reload_button;
    private ProgressBar pb2;
    public LatestNewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toast.makeText(getActivity(), "IN onCreateView", Toast.LENGTH_SHORT).show();

        if (savedInstanceState != null) {
            Toast.makeText(getActivity(), "IN savedInstanceState", Toast.LENGTH_SHORT).show();
            rv.scrollToPosition(savedInstanceState.getInt("scrollPosition",1));
            //Restore the fragment's state here
        }

        View rootView = inflater.inflate(R.layout.fragment_latest_news, container, false);
        rv = rootView.findViewById(R.id.rv_latest_news);
        reload_button = rootView.findViewById(R.id.reload);
        pb2 = rootView.findViewById(R.id.pb2);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        reload_button.setVisibility(View.INVISIBLE);
        pb2.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("country", 0);
        String country = sharedPreferences.getString("country", "us");
        Resources res = getActivity().getResources();

        String URL = res.getString(R.string.request_LatestNews);
        URL = URL + country+"&apiKey=a631133308204b1ba583dc2ed43486b5";

        final NewsAdapter newsAdapter = new NewsAdapter(getContext());
        rv.setAdapter(newsAdapter);


        final StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pb2.setVisibility(View.INVISIBLE);
                Gson gson = new GsonBuilder().create();
                List<Article> articleList = new ArrayList<>();
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray articlesList = responseJson.optJSONArray("articles");
                if (articlesList == null) {
                    return;
                }

                for (int i = 0; i < articlesList.length(); i++) {
                    Article article = gson.fromJson(articlesList.optJSONObject(i).toString(), Article.class);
                    articleList.add(article);
                }
                if (articleList.size() > 0) {
                    newsAdapter.setDataSource(articleList);
                    for (int i = 0; i < articleList.size(); i++) {
                        Utility.insertLatestNewsDb(articleList.get(i),getActivity());
//                      insertLatestNewsDb(articleList.get(i));
                    }

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb2.setVisibility(View.INVISIBLE);
                if (Utility.checkConnectivity(getActivity()))
                    Toast.makeText(getActivity(), "No internet conection", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "Failure to retrieve news", Toast.LENGTH_LONG).show();
                Log.d("TEST", "FAILURE");
            }
        });

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        if(!Utility.checkConnectivity(getActivity()))
        {
            reload_button.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Ensure data connectivity to load news", Toast.LENGTH_LONG).show();
            pb2.setVisibility(View.INVISIBLE);
        }
        else
        {
            pb2.setVisibility(View.INVISIBLE);
            requestQueue.add(stringRequest);
        }

        reload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb2.setVisibility(View.VISIBLE);
                requestQueue.add(stringRequest);
            }
        });
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("LatestNewsFragment", "Setting LatestNewsFragment: ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("scrollPosition",rv.getVerticalScrollbarPosition());
        //Save the fragment's state here
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Toast.makeText(getActivity(), "IN savedInstanceState", Toast.LENGTH_SHORT).show();
            rv.scrollToPosition(savedInstanceState.getInt("scrollPosition",1));
            //Restore the fragment's state here
        }
    }



    @Override
    public void onPause() {
        super.onPause();
    }

    public void insertLatestNewsDb(Article article) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(NewsContract.NewsContractEntry.urlToImage, article.getUrlToImage());
        contentValues.put(NewsContract.NewsContractEntry.author, article.getAuthor());
        contentValues.put(NewsContract.NewsContractEntry.url, article.getUrl());
        contentValues.put(NewsContract.NewsContractEntry.title, article.getTitle());
        contentValues.put(NewsContract.NewsContractEntry.publishedAt, article.getPublishedAt());

        String[] args={article.getUrl()};
        Cursor cursor=getActivity().getContentResolver().query(NewsContract
                .NewsContractEntry.CONTENT_URI2,null,"url=?",args,"timestamp desc");
        if(cursor.getCount()==0)
        {
            getActivity().getContentResolver().insert(NewsContract
                    .NewsContractEntry.CONTENT_URI2, contentValues);
        }
        cursor.close();
    }


}
