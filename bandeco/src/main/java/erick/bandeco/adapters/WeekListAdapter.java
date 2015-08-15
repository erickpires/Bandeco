package erick.bandeco.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bandeco.Constants;
import com.app.bandeco.R;
import com.app.bandeco.Utils;

import java.util.Calendar;

import erick.bandeco.database.DatabaseHelper;
import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;
import erick.bandeco.model.Week;
import erick.bandeco.view.HeightAnimation;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class WeekListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
	private Activity activity;
	private Context context;
	private Week week;
	private boolean shouldDisplayLunch;
	private boolean shouldDisplayDinner;

	private Meal[] lunchArray;
	private Meal[] dinnerArray;

	private int selected;
	private int[] stripesResources;
	private String[] week_days;
	private Utils.MealTextInfo mealTextInfo;
	private String dateFormatString;

	public WeekListAdapter(Activity activity, Week week, boolean shouldDisplayLunch, boolean shouldDisplayDinner, int currentSelected) {
		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.week = week;
		this.shouldDisplayLunch = shouldDisplayLunch;
		this.shouldDisplayDinner = shouldDisplayDinner;
		this.selected = currentSelected;

		if (shouldDisplayLunch) lunchArray = new Meal[7];
		if (shouldDisplayDinner) dinnerArray = new Meal[7];

		mealTextInfo = new Utils.MealTextInfo(context);
		week_days = context.getResources().getStringArray(R.array.days_array);
		dateFormatString = context.getString(R.string.day_of_month);

		fillArrays();
		makeStripesResources();
	}

	public void weekHasChanged(Week week){
		this.week = week;
		fillArrays();
		makeStripesResources();
		notifyDataSetChanged();
	}

	private void makeStripesResources() {
		stripesResources = new int[getCount()];

		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		for(int i = 0; i < stripesResources.length; i++){
			Meal meal = getMeal(i);

			if(Utils.hasDislikedItem(meal, db, context))
				stripesResources[i] = R.drawable.red_strip;

			else if(Utils.hasLikedItem(meal, db, context))
				stripesResources[i] = R.drawable.blue_stripe;
			else
				stripesResources[i] = 0;

		}
		db.close();
	}

	private void fillArrays() {
		for (Day day : week.getDays()) {
			if (shouldDisplayLunch)
				lunchArray[day.getWeekDay()] = day.getLunch();
			if (shouldDisplayDinner)
				dinnerArray[day.getWeekDay()] = day.getDinner();
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
		int count = lunchArray != null ? lunchArray.length : 0;
		count += dinnerArray != null ? dinnerArray.length : 0;
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
	public long getHeaderId(int position) {
		if(shouldDisplayLunch && shouldDisplayDinner)
			return position / 2;

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		boolean isBeenCreated = (convertView == null);
		if (convertView == null) {
			LayoutInflater layoutInflater = activity.getLayoutInflater();
			convertView = layoutInflater.inflate(R.layout.week_list_element, parent, false);
		}

		Meal meal = getMeal(position);

		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView body = (TextView) convertView.findViewById(R.id.body);
		ImageView stripeImageView = (ImageView) convertView.findViewById(R.id.stripe_image_view);

		title.setText(Utils.getMealType(meal, context).toUpperCase());
		title.setTypeface(Utils.getRobotoThin(context));
		body.setText(Utils.getTextFromMeal(meal, mealTextInfo));
		stripeImageView.setBackgroundResource(stripesResources[position]);
		
		boolean isExpanding = position == selected;

		changeItemHeight(convertView, body, parent, isExpanding, !isBeenCreated);

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater layoutInflater = activity.getLayoutInflater();
			convertView = layoutInflater.inflate(R.layout.week_list_header, parent, false);
		}

		int index = (int) getHeaderId(position);


		Day day = getMeal(position).getDay();
		String headerText = week_days[index].toUpperCase();// TODO: + " - " + String.format(dateFormatString, day.getMonthDay(), day.getMonth());

		TextView headerTextView = (TextView) convertView.findViewById(R.id.header_text_view);
		headerTextView.setText(headerText);

		return convertView;
	}

	private Meal getMeal(int position) {
		if (shouldDisplayLunch && shouldDisplayDinner) {
			if (position % 2 == 0)
				return lunchArray[position / 2];
			else
				return dinnerArray[position / 2];
		} else if (shouldDisplayLunch) {
			return lunchArray[position];
		} else {
			return dinnerArray[position];
		}
	}

	private void changeItemHeight(View view, TextView body, View parent, boolean isExpended, boolean shouldAnimate) {
		int maxLines = isExpended ? Integer.MAX_VALUE : Constants.COLLAPSED_MAX_LINES;
		TextUtils.TruncateAt ellipsize = isExpended ? null : TextUtils.TruncateAt.END;

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

		if(startHeight == endHeight)
			return;

		HeightAnimation heightAnimation = new HeightAnimation(view, startHeight, endHeight);
		heightAnimation.setDuration(Constants.HEIGHT_ANIMATION_DURATION);
		view.startAnimation(heightAnimation);
	}
}