package com.app.bandeco;

import html.Html;

import java.util.concurrent.ExecutionException;

import model.Week;
import adapters.TabsAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Main extends FragmentActivity implements TabListener {

	private static final String url = "http://www.nutricao.ufrj.br/cardapio.htm";
	private Html html;
	private Context context;
	private Week week;
	
	private ActionBar actionBar;
	private ViewPager viewPager;
	private TabsAdapter tabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = this;

		if (savedInstanceState != null
				&& savedInstanceState.containsKey("html")){
			System.out.println("Bundle already exists");
			html = (Html) savedInstanceState.getSerializable("html");
		}

		else {
			try {
				html = new HtmlGetter(context).execute(url).get();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
		}
		
		week = new Week(html.getTables());
		
		
		actionBar = getActionBar();
		viewPager = (ViewPager) findViewById(R.id.pager);
		tabsAdapter = new TabsAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(tabsAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		tabsAdapter.setMain(this);
		
		
		Tab tabDay = actionBar.newTab();
		Tab tabWeek = actionBar.newTab();
		
		tabDay.setText("Dia");
		tabDay.setTabListener(this);
		
		tabWeek.setText("Semana");
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
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putSerializable("html", html);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	
		switch (item.getItemId()) {
		case R.id.action_settings:
			//show settings
			System.out.println("update");
			return true;
			
		case R.id.action_update:
			try {
				html = new HtmlGetter(context).execute(url).get();
				Toast.makeText(this, "Cardápio Atualizado", Toast.LENGTH_SHORT).show();
			} catch (Exception e1) {
				e1.printStackTrace();
				Toast.makeText(this, "Problemas ao atualizar: " + e1, Toast.LENGTH_LONG).show();
			}
			
			return true;
			
		case R.id.action_about:
			//show about
			System.out.println("about");
			return true;
		
		default:
			//return super.onOptionsItemSelected(item);
			return false;
		}
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	public Week getWeek() {
		return week;
	}


}
