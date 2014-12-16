package erick.bandeco.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.bandeco.Constants;
import com.app.bandeco.Main;
import com.app.bandeco.R;
import com.app.bandeco.Utils;

import erick.bandeco.adapters.WeekListAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class WeekFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);

		int mealOption = sharedPreferences.getInt(Settings.SHOW_MEALS, Constants.MEAL_OPTION_BOTH_MEALS);

		boolean displayLunch = Utils.shouldDisplayMeal(Constants.MEAL_TYPE_LUNCH, mealOption);
		boolean displayDinner = Utils.shouldDisplayMeal(Constants.MEAL_TYPE_DINNER, mealOption);

		View parentView = inflater.inflate(com.app.bandeco.R.layout.week_fragment, container, false);
		StickyListHeadersListView listView = (StickyListHeadersListView) parentView.findViewById(R.id.week_list_view);

		final WeekListAdapter weekListAdapter = new WeekListAdapter(getActivity(), Main.week, displayLunch, displayDinner);
		listView.setAdapter(weekListAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				weekListAdapter.setSelected(position);
			}
		});

		return parentView;
	}
}