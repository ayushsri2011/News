package com.nightcrawler.news.Activities;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.nightcrawler.news.DataObjects.Source;
import com.nightcrawler.news.Database.FavNewsContract;
import com.nightcrawler.news.R;
import com.nightcrawler.news.Utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookmarksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASK_LOADER_ID = 0;
    private RecyclerView rv;
    ArrayList<Article> temp;
    Cursor cursor;
    Uri uri = FavNewsContract.FavNewsContractEntry.CONTENT_URI;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = (RecyclerView) findViewById(R.id.rv_bookmark_news);
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        newsAdapter = new NewsAdapter(getBaseContext());
        rv.setAdapter(newsAdapter);

        List<Article> articleList = new ArrayList<>();
        newsAdapter.setDataSource(articleList);


        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(TASK_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(TASK_LOADER_ID, null, this);
        } else {
            loaderManager.restartLoader(TASK_LOADER_ID, null, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, BookmarksActivity.this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
//                    String[] args={category};
                    return getContentResolver().query(uri, null, null, null, "timestamp desc");
                } catch (Exception e) {
                    Log.e("loadInBackground()ERROR", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        cursor.moveToFirst();

        temp = new ArrayList<Article>();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No favourites set yet", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
//                Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, Object content
                Article t = new Article(new Source(), cursor.getString(3), cursor.getString(2), "", cursor.getString(1), cursor.getString(4), cursor.getString(0), new Object());
//                t.setPublishedAt(cursor.getString(0));
//                t.setUrl(cursor.getString(1));
//                t.setTitle(cursor.getString(2));
//                t.setAuthor(cursor.getString(3));
//                t.setUrlToImage(cursor.getString(4));
//                t.setDescription("");
                temp.add(t);
                cursor.moveToNext();
            }
        }


        populateRecyclerViewValues(temp);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void populateRecyclerViewValues(ArrayList<Article> movieList) {

        newsAdapter.setDataSource(movieList);
        rv.setAdapter(newsAdapter);
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
