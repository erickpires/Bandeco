package com.app.bandeco;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import erick.bandeco.adapters.TabsAdapter;
import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;
import erick.bandeco.model.Week;
import erick.bandeco.view.About;
import erick.bandeco.view.Settings;

public class Main extends ActionBarActivity {

	public static Week week;

	private TabsAdapter tabsAdapter;
	private DatabaseHelper databaseHelper;
	private ImageButton fab_invite_lunch;
	private ImageButton fab_invite_dinner;

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

		ImageButton fab_invite = (ImageButton) findViewById(R.id.fab_invite);
		fab_invite_lunch = (ImageButton) findViewById(R.id.fab_invite_lunch);
		fab_invite_dinner = (ImageButton) findViewById(R.id.fab_invite_dinner);

		fab_invite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(fab_invite_lunch.getVisibility() == View.GONE){
					fab_invite_lunch.setVisibility(View.VISIBLE);
					fab_invite_dinner.setVisibility(View.VISIBLE);
				}else {
					fab_invite_lunch.setVisibility(View.GONE);
					fab_invite_dinner.setVisibility(View.GONE);
				}
			}
		});

		fab_invite_lunch.setOnClickListener(new InvitationOnClickListener(Constants.MEAL_TYPE_LUNCH));
		fab_invite_dinner.setOnClickListener(new InvitationOnClickListener(Constants.MEAL_TYPE_DINNER));

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
		} catch (IllegalStateException ignored) {
		}

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

			switch (mealType){
				case Constants.MEAL_TYPE_LUNCH :
					meal = today.getLunch();
					break;
				case Constants.MEAL_TYPE_DINNER :
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

			fab_invite_lunch.setVisibility(View.GONE);
			fab_invite_dinner.setVisibility(View.GONE);
		}
	}
}
