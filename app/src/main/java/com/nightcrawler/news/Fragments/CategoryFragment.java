package com.nightcrawler.news.Fragments;

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


public class CategoryFragment extends Fragment {
    private RecyclerView rv_bus;
    private RecyclerView rv_ent;
    private RecyclerView rv_general;
    private RequestQueue requestQueue;
    //    private Context context;
    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        requestQueue = Volley.newRequestQueue(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        rv_bus = rootView.findViewById(R.id.rv_business);
        rv_bus.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, true));

        rv_ent=rootView.findViewById(R.id.rv_ent);
        rv_ent.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, true));

        rv_general=rootView.findViewById(R.id.rv_general);
        rv_general.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, true));

//        Resources res = getActivity().getResources();
//        URL = res.getString(R.string.request_LatestNews);
        String URL_bus = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=a631133308204b1ba583dc2ed43486b5";
        String URL_ent="https://newsapi.org/v2/top-headlines?country=in&category=entertainment&pagesize=50&apiKey=a631133308204b1ba583dc2ed43486b5";
        String URL_general="https://newsapi.org/v2/top-headlines?country=in&category=general&pagesize=50&apiKey=a631133308204b1ba583dc2ed43486b5";
//  Articles arti = new Articles();

        final NewsAdapter newsAdapter_bus = new NewsAdapter(getContext());
        rv_bus.setAdapter(newsAdapter_bus);

        final NewsAdapter newsAdapter_ent = new NewsAdapter(getContext());
        rv_ent.setAdapter(newsAdapter_ent);

        final NewsAdapter newsAdapter_general = new NewsAdapter(getContext());
        rv_general.setAdapter(newsAdapter_general);







        StringRequest stringRequest = new StringRequest(URL_bus, new Response.Listener<String>() {

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
                    newsAdapter_bus.setDataSource(articleList);
                }
                rv_bus.scrollToPosition(0);
                rv_bus.getLayoutManager().scrollToPosition(1);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });

        StringRequest stringRequest1 = new StringRequest(URL_ent, new Response.Listener<String>() {

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
                    newsAdapter_ent.setDataSource(articleList);
                }
                rv_ent.scrollToPosition(0);
                rv_ent.getLayoutManager().scrollToPosition(1);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });


        StringRequest stringRequest3 = new StringRequest(URL_general, new Response.Listener<String>() {

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
                    newsAdapter_general.setDataSource(articleList);
                }
                rv_general.scrollToPosition(0);
                rv_general.getLayoutManager().scrollToPosition(1);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });







        requestQueue.add(stringRequest);
        requestQueue.add(stringRequest1);
        requestQueue.add(stringRequest3);

        return rootView;
    }

}
