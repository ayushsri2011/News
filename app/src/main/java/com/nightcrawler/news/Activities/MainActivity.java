package com.nightcrawler.news.Activities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nightcrawler.news.Fragments.CategoryFragment;
import com.nightcrawler.news.Fragments.LatestNewsFragment;
import com.nightcrawler.news.Fragments.SearchFragment;
import com.nightcrawler.news.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private int mSelectedItem;
    NavigationView navigationView;
    BottomNavigationView navigation;
    FragmentManager fragmentManager = getSupportFragmentManager();
    //    Fragment frag = null;
    LatestNewsFragment latestNewsFragment;
    SearchFragment searchFragment;
    CategoryFragment categoryFragment;
    int doOnce = 0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);

                    for (int i = 0; i < navigationView.getMenu().size(); i++) {
                        navigationView.getMenu().getItem(i).setChecked(false);
                    }

                    selectFragment(item);
//                    onNavigationItemSelected(navigationView.getMenu().getItem(3));
//                    navigationView.getMenu().getItem(0).setChecked(true);x`
//                    navigationView.getMenu().getItem(1).setChecked(true);
//                    navigationView.getMenu().getItem(2).setChecked(true);
//                    navigationView.clearFocus();
//                    mDrawerLayout.clearFocus();
                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_topic);
                    selectFragment(item);
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_search);
                    selectFragment(item);
                    return true;
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


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

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
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
//        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        selectFragment(navigation.getMenu().getItem(0));
        SharedPreferences sharedPreferences = getSharedPreferences("country", 0);
        String country = sharedPreferences.getString("country", "us");
        Toast.makeText(this, " " + country, Toast.LENGTH_SHORT).show();
    }

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




    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
////        if (Utils.checkConnectivity(getBaseContext()))
//        ActionBar actionBar = getSupportActionBar();
//
//        getMenuInflater().inflate(R.menu.settings_menu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        // When the home button is pressed, take the user back to the VisualizerActivity
//        if (id == R.id.action_settings) {
//            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
//            startActivity(startSettingsActivity);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
