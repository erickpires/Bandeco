package com.app.bandeco;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.database.OperationsWithDB;
import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;
import erick.bandeco.model.Week;
import erick.bandeco.controller.About;
import erick.bandeco.controller.Settings;

public class Main extends AppCompatActivity {

	public static Week week;

	private DatabaseHelper databaseHelper;
	private FloatingActionButton inviteLunchFab;
	private FloatingActionButton inviteDinnerFab;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//TODO: week should not be here
		databaseHelper = new DatabaseHelper(getBaseContext());
		updateWeek();
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setLogo(R.drawable.ic_logo);
		setSupportActionBar(toolbar);

		View rootLayout = findViewById(R.id.parent_layout_main);
		Utils.changeStatusColor(this, rootLayout);

		FloatingActionButton inviteFab = (FloatingActionButton) findViewById(R.id.fab_invite);
		inviteLunchFab = (FloatingActionButton) findViewById(R.id.fab_invite_lunch);
		inviteDinnerFab = (FloatingActionButton) findViewById(R.id.fab_invite_dinner);

		inviteFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleFabs();
			}
		});

		inviteFab.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(getApplicationContext(), R.string.share, Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		inviteLunchFab.setOnClickListener(new InvitationOnClickListener(Constants.MEAL_TYPE_LUNCH));
		inviteDinnerFab.setOnClickListener(new InvitationOnClickListener(Constants.MEAL_TYPE_DINNER));

		inviteLunchFab.setOnLongClickListener(new InvitationOnLongClickListener(Constants.MEAL_TYPE_LUNCH));
		inviteDinnerFab.setOnLongClickListener(new InvitationOnLongClickListener(Constants.MEAL_TYPE_DINNER));


		View listView = findViewById(R.id.week_fragment);
		listView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//if (isFabsHidden())
					hideFabs();
				return false;
			}
		});
	}

	@SuppressWarnings("WeakerAccess")
	public void updateWeek() {
		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		week = OperationsWithDB.getWeekFromDatabase(database);
		database.close();
	}

	private void toggleFabs() {
		if (isFabsHidden())
			showFabs();
		else
			hideFabs();
	}

	private boolean isFabsHidden() {
		return inviteLunchFab.getVisibility() != View.VISIBLE &&
					   inviteDinnerFab.getVisibility() != View.VISIBLE;
	}

	private void showFabs() {
		inviteLunchFab.show();
		inviteDinnerFab.show();
	}

	private void hideFabs() {
		inviteLunchFab.hide();
		inviteDinnerFab.hide();
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

	private class InvitationOnClickListener implements View.OnClickListener {
		private int mealType;

		public InvitationOnClickListener(int mealType) {
			this.mealType = mealType;
		}

		@Override
		public void onClick(View v) {
			Day today = week.getToday();
			Meal meal;

			switch (mealType) {
				case Constants.MEAL_TYPE_LUNCH:
					meal = today.getLunch();
					break;
				case Constants.MEAL_TYPE_DINNER:
					meal = today.getDinner();
					break;
				default:
					return;
			}

			String mealBody = Utils.getTextFromMeal(meal, getApplicationContext());
			String mealTypeString = Utils.getMealType(meal, getApplicationContext());

			Intent invitationIntent = Utils.getInvitationIntent(getApplicationContext(), mealTypeString, mealBody);
			Intent invitationIntentChooser = Intent.createChooser(invitationIntent, getString(R.string.share));
			startActivity(invitationIntentChooser);

			hideFabs();
		}
	}

	private class InvitationOnLongClickListener implements View.OnLongClickListener {
		private int mealType;

		public InvitationOnLongClickListener(int mealType) {
			this.mealType = mealType;
		}

		@Override
		public boolean onLongClick(View v) {
			switch (mealType) {
				case Constants.MEAL_TYPE_LUNCH:
					Toast.makeText(getApplicationContext(), R.string.lunch, Toast.LENGTH_SHORT).show();
					break;
				case Constants.MEAL_TYPE_DINNER:
					Toast.makeText(getApplicationContext(), R.string.dinner, Toast.LENGTH_SHORT).show();
					break;
				default:
					return false;
			}

			return true;
		}
	}
}
