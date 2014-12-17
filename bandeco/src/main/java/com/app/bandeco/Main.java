package com.app.bandeco;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;
import erick.bandeco.model.Week;
import erick.bandeco.view.About;
import erick.bandeco.view.Settings;

public class Main extends ActionBarActivity {

	public static Week week;

	private DatabaseHelper databaseHelper;
	private View fab_invite_lunch;
	private View fab_invite_dinner;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//TODO: week should not be here
		databaseHelper = new DatabaseHelper(getBaseContext());
		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		week = OperationsWithDB.getWeekFromDatabase(database);

		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setLogo(R.drawable.ic_logo);
		setSupportActionBar(toolbar);

		View parentLayout = findViewById(R.id.parent_layout_main);

		ImageButton fab_invite = (ImageButton) findViewById(R.id.fab_invite);
		fab_invite_lunch = findViewById(R.id.fab_invite_lunch);
		fab_invite_dinner = findViewById(R.id.fab_invite_dinner);

		fab_invite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleFabs();
			}
		});

		fab_invite.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(getApplicationContext(), R.string.share, Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		fab_invite_lunch.setOnClickListener(new InvitationOnClickListener(Constants.MEAL_TYPE_LUNCH));
		fab_invite_dinner.setOnClickListener(new InvitationOnClickListener(Constants.MEAL_TYPE_DINNER));

		Utils.changeStatusColor(this, parentLayout);

		database.close();
	}

	private void toggleFabs() {
		if(isFabsHidden())
			showFabs();
		else
			hideFabs();
	}

	private boolean isFabsHidden() {
		return fab_invite_lunch.getVisibility() == View.GONE;
	}

	public void hideFabs() {
		fab_invite_lunch.setVisibility(View.GONE);
		fab_invite_dinner.setVisibility(View.GONE);
	}

	public void showFabs() {
		fab_invite_lunch.setVisibility(View.VISIBLE);
		fab_invite_dinner.setVisibility(View.VISIBLE);
	}

	@Override
	public void onResume() {
		super.onResume();
		//TODO: dataSetChange
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
		/*try {
			//TODO: dataSetChange
		} catch (IllegalStateException ignored) {
		}*/

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

			hideFabs();
		}
	}
}
