package com.nightcrawler.news.Activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nightcrawler.news.Fragments.CategoryFragment;
import com.nightcrawler.news.Fragments.LatestNewsFragment;
import com.nightcrawler.news.Fragments.SearchFragment;
import com.nightcrawler.news.R;
import com.nightcrawler.news.Services.UpdateLatestNewsDbService;
import com.nightcrawler.news.Widget.CollectionAppWidgetProvider;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private int mSelectedItem;
    NavigationView navigationView;
    BottomNavigationView navigation;
    FragmentManager fragmentManager = getSupportFragmentManager();
    LatestNewsFragment latestNewsFragment;
    SearchFragment searchFragment;
    CategoryFragment categoryFragment;
    int doOnce = 0;


    public static final String TAG = MainActivity.class
            .getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        Objects.requireNonNull(actionbar).setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        Intent mServiceIntent = new Intent(this, UpdateLatestNewsDbService.class);
        startService(mServiceIntent);

        try{
            CollectionAppWidgetProvider.sendRefreshBroadcast(getBaseContext());
//        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        intent.setComponent(new ComponentName(getBaseContext(), CollectionAppWidgetProvider.class));
//        getBaseContext().sendBroadcast(intent);
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Error in broadcast", Toast.LENGTH_SHORT).show();
        }


        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
        googleAnalytics.setLocalDispatchPeriod(30000);

        Tracker tracker = googleAnalytics.newTracker("UA-129102573-1");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
        tracker.setScreenName("MainActivity");

        Log.i(TAG, "Setting screen name: " + "");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());


        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.Home:
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.preferences:
                                Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                                mDrawerLayout.closeDrawers();
                                startActivity(intent);
                                return true;
                            case R.id.savedArticles:
                                Intent intent2 = new Intent(MainActivity.this, BookmarksActivity.class);
                                mDrawerLayout.closeDrawers();
                                startActivity(intent2);
                                return true;
                            default:
                                return true;
                        }
                    }
                });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        selectFragment(navigation.getMenu().getItem(0));

        SharedPreferences sharedPreferences = getSharedPreferences("country", 0);
        String country = sharedPreferences.getString("country", "us");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    for (int i = 0; i < navigationView.getMenu().size(); i++) {
                        navigationView.getMenu().getItem(i).setChecked(false);
                    }

                    selectFragment(item);
                    return true;
                case R.id.navigation_dashboard:
                    selectFragment(item);
                    return true;
                case R.id.navigation_notifications:
                    selectFragment(item);
                    return true;
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
    };
    private void selectFragment(MenuItem item) {

        if (doOnce == 0) {
            latestNewsFragment = new LatestNewsFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.lay_fragment, latestNewsFragment).commit();
            searchFragment = new SearchFragment();
            categoryFragment = new CategoryFragment();
            doOnce++;

        } else {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction()
                            .replace(R.id.lay_fragment, latestNewsFragment).commit();
                    break;

                case R.id.navigation_dashboard:
                    if (categoryFragment == null) {
                        categoryFragment = new CategoryFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.lay_fragment, categoryFragment).commit();
                    } else {
                        fragmentManager.beginTransaction()
                                .replace(R.id.lay_fragment, categoryFragment).commit();
                    }
                    break;

                case R.id.navigation_notifications:
                    if (searchFragment == null) {
                        searchFragment = new SearchFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.lay_fragment, searchFragment).commit();
                    } else {
                        fragmentManager.beginTransaction()
                                .replace(R.id.lay_fragment, searchFragment).commit();
                    }
                    break;
            }

            // update selected item
            mSelectedItem = item.getItemId();

            // uncheck the other items.
            for (int i = 0; i < navigation.getMenu().size(); i++) {
                MenuItem menuItem = navigation.getMenu().getItem(i);
                menuItem.setChecked(menuItem.getItemId() == item.getItemId());
            }

//            updateToolbarText(item.getTitle());

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        mDrawerLayout.closeDrawers();

        MenuItem homeItem = navigation.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            selectFragment(homeItem);
            navigation.setSelectedItemId(R.id.navigation_home);
        } else {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            mBuilder.setTitle("Exit App?");

            mBuilder.setCancelable(true);
            mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    finish();
                }
            });

            mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        }
    }

}
