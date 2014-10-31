package com.app.bandeco;

import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.model.Week;
import erick.bandeco.adapters.TabsAdapter;
import erick.bandeco.view.About;
import erick.bandeco.view.Settings;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import static android.support.v7.app.ActionBar.Tab;

public class Main extends ActionBarActivity {

    public static Week week;

    //private ActionBar actionBar;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        week = OperationsWithDB.getWeekFromDatabase(database);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);

        database.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                return true;

            case R.id.action_update:
                Intent intent = new Intent(getApplicationContext(), UpdateService.class);
                startService(intent);

                return true;

            case R.id.action_about:
                startActivity(new Intent(this, About.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
