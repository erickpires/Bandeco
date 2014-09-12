package com.app.bandeco;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import html.Html;
import model.Week;
import adapters.TabsAdapter;

import android.content.Context;
import android.content.Intent;
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

    private Html html;
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

        System.out.println("HTML is: " + html);

        File saved = new File(getFilesDir(), "saved.inst");

		/*if (saved.exists()) {
            System.out.println("There's a saved file instance");
			createHtmlFromFile(saved);
		} else*/
        createHtml();

        System.out.println("HTML is: " + html);

        Calendar now = Calendar.getInstance();
        Calendar htmlCalendar = html.getCalendar();

        if ((now.get(Calendar.WEEK_OF_YEAR) > htmlCalendar
                .get(Calendar.WEEK_OF_YEAR) || now.get(Calendar.YEAR) > htmlCalendar
                .get(Calendar.YEAR))
                && now.get(Calendar.DAY_OF_WEEK) > Calendar.SUNDAY) {

            createHtml();
            System.out.println("File is not up-to-dated");
        }

        saveHtmlInstance(saved);

        week = new Week(html.getTables());

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.pager);
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

    private void createHtml() {

        try {
            html = new HtmlGetter(context).execute(ApplicationHelper.url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //new Thread(r).run();
    }

    private void createHtmlFromFile(File saved) {
        FileInputStream inputStream;
        ObjectInputStream objectInputStream;
        try {
            inputStream = new FileInputStream(saved);
            objectInputStream = new ObjectInputStream(inputStream);

            html = (Html) objectInputStream.readObject();

            objectInputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveHtmlInstance(File saved) {
        saved.delete();

        FileOutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        try {
            outputStream = new FileOutputStream(saved);
            objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(html);

            objectOutputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
