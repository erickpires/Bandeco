package erick.bandeco.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.bandeco.Constants;
import com.app.bandeco.Main;
import com.app.bandeco.R;
import com.app.bandeco.Utils;

import java.util.ArrayList;

import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;
import erick.bandeco.model.Week;

public class WeekFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);

		int mealOption = sharedPreferences.getInt(Settings.SHOW_MEALS, Constants.MEAL_OPTION_BOTH_MEALS);

		boolean displayLunch = Utils.shouldDisplayMeal(Constants.MEAL_TYPE_LUNCH, mealOption);
		boolean displayDinner = Utils.shouldDisplayMeal(Constants.MEAL_TYPE_DINNER, mealOption);

		View parentView = inflater.inflate(com.app.bandeco.R.layout.week_fragment, container, false);
		ListView listView = (ListView) parentView.findViewById(R.id.week_list_view);

		WeekListAdapter weekListAdapter = new WeekListAdapter(getActivity().getApplicationContext(), Main.week, displayLunch, displayDinner);
		listView.setAdapter(weekListAdapter);

		return parentView;
	}

	private class WeekListAdapter extends BaseAdapter {
		Context context;
		Week week;
		boolean shouldDisplayLunch;
		boolean shouldDisplayDinner;

		ArrayList <Meal> lunchList;
		ArrayList <Meal> dinnerList;

		public WeekListAdapter(Context context, Week week, boolean shouldDisplayLunch, boolean shouldDisplayDinner) {
			this.context = context;
			this.week = week;
			this.shouldDisplayLunch = shouldDisplayLunch;
			this.shouldDisplayDinner = shouldDisplayDinner;

			if (shouldDisplayLunch) lunchList = new ArrayList<Meal>();
			if (shouldDisplayDinner) dinnerList = new ArrayList<Meal>();

			fillLists();
		}

		private void fillLists() {
			for(Day day : week.getDays()){
				if(shouldDisplayLunch)
					lunchList.add(day.getLunch());
				if(shouldDisplayDinner)
					dinnerList.add(day.getDinner());
			}
		}

		@Override
		public int getCount() {
			int count = lunchList != null ? lunchList.size() : 0;
			count += dinnerList != null ? dinnerList.size() : 0;
			return count;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater layoutInflater = getActivity().getLayoutInflater();
				convertView = layoutInflater.inflate(R.layout.week_list_element, parent, false);
			}

			ArrayList <Meal> array_ptr;
			int index;

			if(shouldDisplayLunch && shouldDisplayDinner){
				if (position % 2 == 0)
					array_ptr = lunchList;
				else
					array_ptr = dinnerList;

				index = position / 2;
			}else if(shouldDisplayLunch){
				array_ptr = lunchList;
				index = position;
			}else{
				array_ptr = dinnerList;
				index = position;
			}

			Meal meal = array_ptr.get(index);

			TextView title = (TextView) convertView.findViewById(R.id.title);
			TextView body = (TextView) convertView.findViewById(R.id.body);

			title.setText(Utils.getMealType(meal, context));
			body.setText(Utils.getTextFromMeal(meal, context));

			return convertView;
		}
	}
}