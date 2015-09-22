package erick.bandeco.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.app.bandeco.Constants;
import com.app.bandeco.Main;
import com.app.bandeco.R;
import com.app.bandeco.Utils;

import java.util.Calendar;

import erick.bandeco.adapters.WeekListAdapter;
import erick.bandeco.model.Day;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class WeekFragment extends Fragment {

	private boolean displayDinner;
	private boolean displayLunch;
	private StickyListHeadersListView listView;
	private WeekListAdapter weekListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);

		int mealOption = sharedPreferences.getInt(Settings.SHOW_MEALS, Constants.MEAL_OPTION_BOTH_MEALS);

		displayLunch = Utils.shouldDisplayMeal(Constants.MEAL_TYPE_LUNCH, mealOption);
		displayDinner = Utils.shouldDisplayMeal(Constants.MEAL_TYPE_DINNER, mealOption);
		int nextMealPosition = getNextMealPositionInList();

		View parentView = inflater.inflate(com.app.bandeco.R.layout.week_fragment, container, false);
		listView = (StickyListHeadersListView) parentView.findViewById(R.id.week_list_view);

		weekListAdapter = new WeekListAdapter(getActivity(), Main.week, displayLunch, displayDinner, nextMealPosition);
		listView.setAdapter(weekListAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				weekListAdapter.setSelected(position);
			}
		});

		return parentView;
	}

	LocalBroadcastManager localBroadcastManager;
	@Override
	public void onResume() {
		super.onResume();

		int nextMealPosition = getNextMealPositionInList();
		listView.setSelection(nextMealPosition);
		weekListAdapter.setSelected(nextMealPosition);

		localBroadcastManager = LocalBroadcastManager.getInstance(getActivity().getApplicationContext());
		localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter("update_event"));
	}

	@Override
	public void onPause() {
		super.onPause();

		localBroadcastManager.unregisterReceiver(messageReceiver);
	}

	private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			weekListAdapter.weekHasChanged(Main.week);
		}
	};

	int getNextMealPositionInList() {
		Calendar today = Calendar.getInstance();
		int dayNumber = Day.adaptDayOfWeek(today.get(Calendar.DAY_OF_WEEK));

		if (displayLunch && displayDinner) {
			if (today.get(Calendar.HOUR_OF_DAY) < 14)
				return 2 * dayNumber;
			else
				return 2 * dayNumber + 1;
		}

		return dayNumber;
	}
}