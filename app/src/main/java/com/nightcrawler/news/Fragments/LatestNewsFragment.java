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


public class LatestNewsFragment extends Fragment {
    private RecyclerView rv;

    public LatestNewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_latest_news, container, false);
        rv = rootView.findViewById(R.id.rv_latest_news);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        Resources res = getActivity().getResources();
        String URL = res.getString(R.string.request_LatestNews);
        URL = URL + "us&apiKey=a631133308204b1ba583dc2ed43486b5";

//        Articles arti = new Articles();
        final NewsAdapter newsAdapter = new NewsAdapter(getContext());
        rv.setAdapter(newsAdapter);


        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {

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

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        return rootView;
    }

}
