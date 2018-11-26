package com.nightcrawler.news.Fragments;

import android.content.SharedPreferences;
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
import java.util.Objects;


public class CategoryFragment extends Fragment {
    private RecyclerView rv_bus, rv_ent, rv_general, rv_technology, rv_sports, rv_health, rv_science;
     NewsAdapter newsAdapter_bus;// = new NewsAdapter(getContext());
     NewsAdapter newsAdapter_ent;// = new NewsAdapter(getContext());
     NewsAdapter newsAdapter_general;// = new NewsAdapter(getContext());
     NewsAdapter newsAdapter_health;// = new NewsAdapter(getContext());
     NewsAdapter newsAdapter_science;// = new NewsAdapter(getContext());
     NewsAdapter newsAdapter_sports;// = new NewsAdapter(getContext());
     NewsAdapter newsAdapter_technology;// = new NewsAdapter(getContext());

    private RequestQueue requestQueue;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Toast.makeText(getActivity(), R.string.swipe_across, Toast.LENGTH_SHORT).show();

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        rv_bus = rootView.findViewById(R.id.rv_business);
        rv_bus.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rv_ent = rootView.findViewById(R.id.rv_ent);
        rv_ent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rv_general = rootView.findViewById(R.id.rv_general);
        rv_general.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rv_health = rootView.findViewById(R.id.rv_health);
        rv_health.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rv_science = rootView.findViewById(R.id.rv_science);
        rv_science.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rv_sports = rootView.findViewById(R.id.rv_sports);
        rv_sports.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rv_technology = rootView.findViewById(R.id.rv_technology);
        rv_technology.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


          newsAdapter_bus = new NewsAdapter(getContext());
          newsAdapter_ent = new NewsAdapter(getContext());
          newsAdapter_general = new NewsAdapter(getContext());
          newsAdapter_health = new NewsAdapter(getContext());
          newsAdapter_science = new NewsAdapter(getContext());
          newsAdapter_sports = new NewsAdapter(getContext());
          newsAdapter_technology = new NewsAdapter(getContext());




        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("country", 0);
        String country = sharedPreferences.getString("country", "us");

        Resources res = getActivity().getResources();
        String Base_URL = res.getString(R.string.request_LatestNews);
        String API_Key = res.getString(R.string.apiKey);

        String URL_bus = Base_URL+country+"&category="+res.getString(R.string.category_business)+"&pagesize=50&apiKey="+API_Key;
        String URL_ent = Base_URL+country+"&category="+res.getString(R.string.category_entertainment)+"&pagesize=50&apiKey="+API_Key;
        String URL_general = Base_URL+country+"&category="+res.getString(R.string.category_general)+"&pagesize=50&apiKey="+API_Key;
        String URL_health = Base_URL+country+"&category="+res.getString(R.string.category_health)+"&pagesize=50&apiKey="+API_Key;
        String URL_science = Base_URL+country+"&category="+res.getString(R.string.category_science)+"&pagesize=50&apiKey="+API_Key;
        String URL_sports = Base_URL+country+"&category="+res.getString(R.string.category_sports)+"&pagesize=50&apiKey="+API_Key;
        String URL_technology = Base_URL+country+"&category="+res.getString(R.string.category_technology)+"&pagesize=50&apiKey="+API_Key;

        rv_bus.setAdapter(newsAdapter_bus);
        rv_ent.setAdapter(newsAdapter_ent);
        rv_general.setAdapter(newsAdapter_general);
        rv_health.setAdapter(newsAdapter_health);
        rv_science.setAdapter(newsAdapter_science);
        rv_sports.setAdapter(newsAdapter_sports);
        rv_technology.setAdapter(newsAdapter_technology);

        ArrayList<String> requestURL = new ArrayList<>();
        requestURL.add(URL_bus);
        requestURL.add(URL_ent);
        requestURL.add(URL_general);
        requestURL.add(URL_health);
        requestURL.add(URL_science);
        requestURL.add(URL_sports);
        requestURL.add(URL_technology);
        requestQueues(requestURL);

        return rootView;
    }


    public void requestQueues(ArrayList<String> requestURL) {

        StringRequest stringRequest1 = new StringRequest(requestURL.get(0), new Response.Listener<String>() {

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
                    newsAdapter_bus.setDataSource(articleList);
                }
                rv_bus.scrollToPosition(0);
                rv_bus.getLayoutManager().scrollToPosition(0);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });

        StringRequest stringRequest2 = new StringRequest(requestURL.get(1), new Response.Listener<String>() {

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
                    newsAdapter_ent.setDataSource(articleList);
                }
                rv_ent.scrollToPosition(0);
                rv_ent.getLayoutManager().scrollToPosition(0);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });


        StringRequest stringRequest3 = new StringRequest(requestURL.get(2), new Response.Listener<String>() {

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
                rv_general.getLayoutManager().scrollToPosition(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });


        StringRequest stringRequest4 = new StringRequest(requestURL.get(3), new Response.Listener<String>() {

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
                    newsAdapter_health.setDataSource(articleList);
                }
                rv_health.scrollToPosition(0);
                rv_health.getLayoutManager().scrollToPosition(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });

        StringRequest stringRequest5 = new StringRequest(requestURL.get(4), new Response.Listener<String>() {

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
                    newsAdapter_science.setDataSource(articleList);
                }
                rv_science.scrollToPosition(0);
                rv_science.getLayoutManager().scrollToPosition(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });

        StringRequest stringRequest6 = new StringRequest(requestURL.get(5), new Response.Listener<String>() {

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
                    newsAdapter_sports.setDataSource(articleList);
                }
                rv_sports.scrollToPosition(0);
                rv_sports.getLayoutManager().scrollToPosition(0);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });

        StringRequest stringRequest7 = new StringRequest(requestURL.get(6), new Response.Listener<String>() {

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
                    newsAdapter_technology.setDataSource(articleList);
                }
                rv_technology.scrollToPosition(0);
                rv_technology.getLayoutManager().scrollToPosition(0);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "FAILURE");
            }
        });

        requestQueue.add(stringRequest1);
        requestQueue.add(stringRequest2);
        requestQueue.add(stringRequest3);
        requestQueue.add(stringRequest4);
        requestQueue.add(stringRequest5);
        requestQueue.add(stringRequest6);
        requestQueue.add(stringRequest7);
    }

}
