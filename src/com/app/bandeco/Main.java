package com.app.bandeco;

import html.Html;
import java.util.concurrent.ExecutionException;

import view.MealCard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Main extends Activity {

	private static final String url = "http://www.nutricao.ufrj.br/cardapio.htm";
	private Html html;
	private Context context;
	private Week week;

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

		final Spinner daySpinner = (Spinner) findViewById(R.id.day_spinner);
		ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter
				.createFromResource(this, R.array.days_array,
						android.R.layout.simple_spinner_item);

		daysAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		daySpinner.setAdapter(daysAdapter);;
		final MealCard mealCard = (MealCard) findViewById(R.id.mealCard1);

		daySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				mealCard.setDay(week.getDay(pos));
				mealCard.setMeal(week.getDay(pos).getLunch());

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
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
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
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

}
