package com.app.bandeco;

import html.Html;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Main extends Activity {

	private static final String url = "http://www.nutricao.ufrj.br/cardapio.htm";
	private Integer currentColumn = 1;
	private Integer currentRow = 4;
	private Html html;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Context context = this;

		if (savedInstanceState != null
				&& savedInstanceState.containsKey("html"))
			html = (Html) savedInstanceState.getSerializable("html");

		else {
			try {
				html = new HtmlGetter(context).execute(url).get();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
		}

		final Spinner daySpinner = (Spinner) findViewById(R.id.day_spinner);
		ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter
				.createFromResource(this, R.array.days_array,
						android.R.layout.simple_spinner_item);

		daysAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		daySpinner.setAdapter(daysAdapter);

		final Spinner mealSpinner = (Spinner) findViewById(R.id.meal_spinner);
		ArrayAdapter<CharSequence> mealAdapter = ArrayAdapter
				.createFromResource(this, R.array.meals_array,
						android.R.layout.simple_spinner_item);

		mealAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mealSpinner.setAdapter(mealAdapter);

		final Button b = (Button) findViewById(R.id.button1);
		final TextView text = (TextView) findViewById(R.id.textView1);

		daySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				currentColumn = pos + 1;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mealSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos == 0)
					currentRow = 4;
				if (pos == 1)
					currentRow = 6;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				List<String> cardapio = html.getTables().get(currentRow)
						.getColumn(currentColumn);

				String dia = "";

				for (String s : cardapio)
					dia += s + "\n";

				text.setText(dia);

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

}
