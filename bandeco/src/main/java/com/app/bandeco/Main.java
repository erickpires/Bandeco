package com.app.bandeco;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import database.DatabaseHelper;
import html.Html;
import model.Week;
import adapters.TabsAdapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static android.support.v7.app.ActionBar.Tab;

public class Main extends ActionBarActivity implements ActionBar.TabListener {

    private Context context;
    public static Week week;

    private ActionBar actionBar;
    private ViewPager viewPager;
    private TabsAdapter tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        week = ApplicationHelper.getWeekFromDatabase(database);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.pager);

        database.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabsAdapter);

        actionBar.removeAllTabs();

        Tab tabDay = actionBar.newTab();
        Tab tabWeek = actionBar.newTab();

        tabDay.setText(getString(R.string.hoje));
        tabDay.setTabListener(this);

        tabWeek.setText(getString(R.string.semana));
        tabWeek.setTabListener(this);

        actionBar.addTab(tabDay);
        actionBar.addTab(tabWeek);

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                actionBar.setSelectedNavigationItem(pos);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.action_settings:
                // show settings
                startActivity(new Intent(this, Settings.class));
                return true;

            case R.id.action_update:
                //createHtml();

                Intent intent = new Intent(getApplicationContext(), UpdateService.class);
                startService(intent);

                return true;

            case R.id.action_about:
                // show about
                startActivity(new Intent(this, About.class));
                return true;

            default:
                // return super.onOptionsItemSelected(item);
                return false;
        }
    }

    // @Override
    // public void onTabReselected(Tab tab, FragmentTransaction ft) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onTabSelected(Tab tab, FragmentTransaction ft) {
    // viewPager.setCurrentItem(tab.getPosition());
    // }
    //
    // @Override
    // public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    // // TODO Auto-generated method stub
    //
    // }

    @Override
    public void onTabSelected(Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabReselected(Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        // TODO Auto-generated method stub
    }
}
