package com.nightcrawler.news.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.nightcrawler.news.DataObjects.Articles;
import com.nightcrawler.news.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private RecyclerView rv;
    private TextView search_text;
    private Button refresh;
    //    private Context context;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        rv = rootView.findViewById(R.id.rv_search);
        final NewsAdapter newsAdapter = new NewsAdapter(getContext());
        rv.setAdapter(newsAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        search_text = rootView.findViewById(R.id.search_text);
        refresh = rootView.findViewById(R.id.refresh);
        Resources res = getActivity().getResources();
        String URL = res.getString(R.string.request_LatestNews);
        URL = URL + "us&apiKey=a631133308204b1ba583dc2ed43486b5";

        final String finalURL = URL;
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                StringRequest stringRequest = new StringRequest(finalURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                        }

//                    Log.d("TEST", "Not null");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                        Log.d("TEST","FAILURE");
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
        });


        return rootView;
    }

}