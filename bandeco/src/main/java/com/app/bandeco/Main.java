package com.app.bandeco;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import html.Html;
import model.Week;
import adapters.TabsAdapter;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import android.widget.Toast;

public class Main extends SherlockFragmentActivity implements TabListener {

	// private static final String url =
	// "http://www.nutricao.ufrj.br/cardapio.htm";
	private static final String url = "http://www.nutricao.ufrj.br/cardapio.htm";
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

		File saved = new File(getFilesDir(), "saved.inst");

		if (saved.exists()) {
			System.out.println("There's a saved file instance");
			createHtmlFromFile(saved);
		} else
			createHtml();

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
    public void onResume(){
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
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {
		case R.id.action_settings:
			// show settings
			System.out.println("update");
			return true;

		case R.id.action_update:
			createHtml();
			Toast.makeText(this, getString(R.string.cardapio_atualizado), Toast.LENGTH_SHORT)
					.show();
			return true;

		case R.id.action_about:
			// show about
			System.out.println("about");
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
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					html = new HtmlGetter(context).execute(url).get();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		new Thread(r).run();
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
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {

		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
