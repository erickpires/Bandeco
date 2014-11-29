package erick.bandeco.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.app.bandeco.R;
import com.app.bandeco.Utils;
import com.haarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;

import erick.bandeco.model.Meal;

public class MyExpandableListViewAdapter extends
		ExpandableListItemAdapter<Meal> {

	private Context context;

	public MyExpandableListViewAdapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {

		Meal meal = getItem(position);

		LinearLayout rl = (LinearLayout) convertView;
		if (rl == null) {
			rl = new LinearLayout(context);
		}

		rl.removeAllViews();

		TextView tv = new TextView(context);

		tv.setText(Utils.getTextFromMeal(meal, context));

		tv.setBackgroundResource(com.app.bandeco.R.drawable.card_bg);

		tv.setTextSize(13);
		tv.setTextColor(Color.parseColor("#424242"));
		tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
		tv.setPadding(8, 5, 48, 0);
		tv.setGravity(Gravity.CENTER | Gravity.LEFT);

		tv.setLines(calculateMagicNumber()); //This is a workaround (gambiarra) and you should not care about it

		int margin = context.getResources().getDimensionPixelSize(R.dimen.my_margin);

		if (meal.getPratoPrincipal().toLowerCase().contains("peixe"))
			tv.setBackgroundResource(com.app.bandeco.R.drawable.card_bg_fish);

		else
			tv.setBackgroundResource(com.app.bandeco.R.drawable.card_bg);

		rl.addView(tv);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv
																			   .getLayoutParams();
		params.setMargins(margin, 0, margin, 0); // substitute parameters for left, top,
		// right, bottom
		params.width = LinearLayout.LayoutParams.MATCH_PARENT;
		params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
		tv.setLayoutParams(params);

		return rl;
	}

	@Override
	public View getTitleView(int position, View convertView, ViewGroup parent) {

		Meal meal = getItem(position);

		RelativeLayout rl = (RelativeLayout) convertView;
		if (rl == null) {
			rl = new RelativeLayout(context);
		}

		TextView tv = new TextView(context);

		tv.setText(Utils.getMealType(meal, context) + ", " + Utils.getDayOfTheWeek(meal.getDay(), context));
		tv.setClickable(false);

		tv.setTextSize(18);
		tv.setTextColor(Color.parseColor("#7094FF"));
		tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
		tv.setPadding(8, 0, 0, 0);
		tv.setGravity(Gravity.CENTER);
		tv.setLines(3);


		int margin = context.getResources().getDimensionPixelSize(R.dimen.my_margin);

		tv.setBackgroundResource(com.app.bandeco.R.drawable.card_bg_title);
		rl.addView(tv);
		RelativeLayout.LayoutParams params = (LayoutParams) tv
																	.getLayoutParams();
		params.setMargins(margin, 0, margin, 0); // substitute parameters for left, top,
		// right, bottom
		params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
		params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		tv.setLayoutParams(params);

		return rl;
	}

	@SuppressWarnings("deprecation")
	private int calculateMagicNumber() {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display d = wm.getDefaultDisplay();

		int width = d.getWidth();

		if (width < 300)
			return 15;
		if (width < 400)
			return 13;
		if (width < 750)
			return 11;
		else
			return 8;
	}
}
