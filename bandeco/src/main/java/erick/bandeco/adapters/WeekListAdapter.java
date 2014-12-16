package erick.bandeco.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.bandeco.Constants;
import com.app.bandeco.R;
import com.app.bandeco.Utils;

import java.util.ArrayList;

import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;
import erick.bandeco.model.Week;
import erick.bandeco.view.HeightAnimation;

public class WeekListAdapter extends BaseAdapter {
	private Activity activity;
	private Context context;
	private Week week;
	private boolean shouldDisplayLunch;
	private boolean shouldDisplayDinner;

	ArrayList<Meal> lunchList;
	ArrayList<Meal> dinnerList;

	private int selected = 0;

	public WeekListAdapter(Activity activity, Week week, boolean shouldDisplayLunch, boolean shouldDisplayDinner) {
		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.week = week;
		this.shouldDisplayLunch = shouldDisplayLunch;
		this.shouldDisplayDinner = shouldDisplayDinner;

		if (shouldDisplayLunch) lunchList = new ArrayList<Meal>();
		if (shouldDisplayDinner) dinnerList = new ArrayList<Meal>();

		fillLists();
	}

	private void fillLists() {
		for (Day day : week.getDays()) {
			if (shouldDisplayLunch)
				lunchList.add(day.getLunch());
			if (shouldDisplayDinner)
				dinnerList.add(day.getDinner());
		}
	}

	public void setSelected(int selected) {
		if (this.selected == selected)
			return;
		this.selected = selected;
		notifyDataSetChanged();
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
		boolean isBeenCreated = convertView == null;
		if (convertView == null) {
			LayoutInflater layoutInflater = activity.getLayoutInflater();
			convertView = layoutInflater.inflate(R.layout.week_list_element, parent, false);
		}

		Meal meal = getMeal(position);

		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView body = (TextView) convertView.findViewById(R.id.body);

		title.setText(Utils.getMealType(meal, context).toUpperCase());
		body.setText(Utils.getTextFromMeal(meal, context));

		boolean isExpanding = position == selected;

		changeItemHeight(convertView, body, parent, isExpanding, !isBeenCreated);

		return convertView;
	}

	private Meal getMeal(int position) {
		if (shouldDisplayLunch && shouldDisplayDinner) {
			if (position % 2 == 0)
				return lunchList.get(position / 2);
			else
				return dinnerList.get(position / 2);
		} else if (shouldDisplayLunch) {
			return lunchList.get(position);
		} else {
			return dinnerList.get(position);
		}
	}

	private void changeItemHeight(View view, TextView body, View parent, boolean isExpending, boolean shouldAnimate) {
		int maxLines = isExpending ? Integer.MAX_VALUE : Constants.COLLAPSED_MAX_LINES;
		TextUtils.TruncateAt ellipsize = isExpending ? null : TextUtils.TruncateAt.END;

		body.setEllipsize(ellipsize);

		if (!shouldAnimate) {
			body.setMaxLines(maxLines);
			return;
		}

		int startHeight = view.getMeasuredHeight();
		int endHeight;

		body.setMaxLines(maxLines);

		view.measure(View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY),
							View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

		endHeight = view.getMeasuredHeight();

		HeightAnimation heightAnimation = new HeightAnimation(view, startHeight, endHeight);
		heightAnimation.setDuration(Constants.ANIMATION_DURATION);
		view.startAnimation(heightAnimation);
	}
}