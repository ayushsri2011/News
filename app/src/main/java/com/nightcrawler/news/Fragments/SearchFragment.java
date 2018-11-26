package com.nightcrawler.news.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.nightcrawler.news.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private RecyclerView rv;
    private TextView search_text;
    private ImageButton refresh;
    private ProgressBar pb3;
    NewsAdapter newsAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        rv = rootView.findViewById(R.id.rv_search);
        pb3 = rootView.findViewById(R.id.pb3);
        pb3.setVisibility(View.INVISIBLE);

        newsAdapter = new NewsAdapter(getContext());
        rv.setAdapter(newsAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        search_text = rootView.findViewById(R.id.search_text);
        refresh = rootView.findViewById(R.id.refresh);

//        final String finalURL = "https://newsapi.org/v2/top-headlines?q=";


        search_text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    processRequest();
                    return true;
                }
                return false;
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processRequest();
            }
        });
        return rootView;
    }


    public void processRequest() {

        pb3.setVisibility(View.VISIBLE);
        String text = search_text.getText().toString();
        Resources res=getResources();
        String t=res.getString(R.string.request_SearchNews);
        if (!t.trim().equals("")) {
            t += text;
            t += "&apiKey="+res.getString(R.string.apiKey);

            StringRequest stringRequest = new StringRequest(t, new Response.Listener<String>() {
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
                    JSONArray articlesList = Objects.requireNonNull(responseJson).optJSONArray("articles");
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
                    pb3.setVisibility(View.INVISIBLE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Unable to fetch results", Toast.LENGTH_SHORT).show();
                    pb3.setVisibility(View.INVISIBLE);
                    Log.d("TEST", "FAILURE");
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }
    }
