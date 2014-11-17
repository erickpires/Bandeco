package com.app.bandeco;

import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.model.Week;
import erick.bandeco.adapters.TabsAdapter;
import erick.bandeco.view.About;
import erick.bandeco.view.Settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Main extends ActionBarActivity {

    public static Week week;

    private TabsAdapter tabsAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getBaseContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        week = OperationsWithDB.getWeekFromDatabase(database);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_logo);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);

        View parentLayout = findViewById(R.id.parent_layout_main);

        Utils.changeStatusColor(this, parentLayout);

        database.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        tabsAdapter.dataChanged();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("update_event"));
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateData();
        }
    };

    private void updateData() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        week = OperationsWithDB.getWeekFromDatabase(database);
        database.close();
        try {
            tabsAdapter.dataChanged();
        }catch (IllegalStateException ignored){}

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
