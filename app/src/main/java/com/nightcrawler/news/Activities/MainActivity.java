package com.nightcrawler.news.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.nightcrawler.news.Fragments.CategoryFragment;
import com.nightcrawler.news.Fragments.LatestNewsFragment;
import com.nightcrawler.news.Fragments.SearchFragment;
import com.nightcrawler.news.R;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private int mSelectedItem;
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
                    mTextMessage.setText(R.string.title_home);
                    selectFragment(item);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_topic);
                    selectFragment(item);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_search);
                    selectFragment(item);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        selectFragment(navigation.getMenu().getItem(0));
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

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    @Override
    public void onBackPressed() {
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
