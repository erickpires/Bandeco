package com.app.bandeco;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
	private View fabInviteLunch;
	private View fabInviteDinner;
	private boolean fabsAreVisible;

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

		View parentLayout = findViewById(R.id.parent_layout_main);

		ImageButton fab_invite = (ImageButton) findViewById(R.id.fab_invite);
		fabInviteLunch = findViewById(R.id.fab_invite_lunch);
		fabInviteDinner = findViewById(R.id.fab_invite_dinner);
		View fabInviteLunchImage = findViewById(R.id.fab_invite_lunch_image);
		View fabInviteDinnerImage = findViewById(R.id.fab_invite_dinner_image);

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

		fabInviteLunch.setOnClickListener(new InvitationOnClickListener(Constants.MEAL_TYPE_LUNCH));
		fabInviteDinner.setOnClickListener(new InvitationOnClickListener(Constants.MEAL_TYPE_DINNER));

		ViewCompat.setElevation(fab_invite, 5f);
		ViewCompat.setElevation(fabInviteLunchImage, 5f);
		ViewCompat.setElevation(fabInviteDinnerImage, 5f);

		Utils.changeStatusColor(this, parentLayout);
	}

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

	public boolean isFabsHidden() {
		return !fabsAreVisible;
	}

	public void showFabs() {
		fabsAreVisible = true;
		showWithTranslation(fabInviteLunch);
		showWithTranslation(fabInviteDinner);
	}

	public void hideFabs() {
		fabsAreVisible = false;
		hideWithTranslation(fabInviteLunch);
		hideWithTranslation(fabInviteDinner);
	}

	private void showWithTranslation(final View view) {
		//TODO: 500 is big enough? this value should be calculated, not be a magic number
		int displacement = 500;
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, displacement, 0);
		translateAnimation.setDuration(Constants.SHOW_ANIMATION_DURATION);

		translateAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				view.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		view.startAnimation(translateAnimation);
	}

	private void hideWithTranslation(final View view) {
		//TODO: 500 is big enough? this value should be calculated, not be a magic number
		int displacement = 500;
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, displacement);
		//TODO: magic number
		translateAnimation.setDuration(Constants.HIDE_ANIMATION_DURATION);

		translateAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.GONE);
			}
		});

		view.startAnimation(translateAnimation);
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
}
