package adapters;

import model.Meal;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.renderscript.Program.TextureType;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.app.bandeco.Main;
import com.haarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;

public class MyExpandableListViewAdapter extends
		ExpandableListItemAdapter<Meal> {

	private Context context;
	private Main main;

	public MyExpandableListViewAdapter(Context context) {
		super(context);
		// super(context,
		// com.app.bandeco.R.layout.activity_expandablelistitem_card,
		// com.app.bandeco.R.id.ListView1,
		// com.app.bandeco.R.id.activity_expandablelistitem_card_content);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		
		Meal meal = getItem(position);

		LinearLayout rl = (LinearLayout) convertView;
		if (rl == null) {
			rl = new LinearLayout(context);
		}

		TextView tv = new TextView(context);

		tv.setText("" + meal );

		tv.setBackgroundResource(com.app.bandeco.R.drawable.card_bg);

		tv.setTextSize(13);
		tv.setTextColor(Color.parseColor("#424242"));
		tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
		tv.setPadding(8, 5, 48, 0);
		//tv.setEms(20);
		tv.setGravity(Gravity.CENTER | Gravity.LEFT);

		tv.setCompoundDrawablePadding(15);
		//tv.setLines(12);
		
		tv.setBackgroundResource(com.app.bandeco.R.drawable.card_bg);
		rl.addView(tv);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv
				.getLayoutParams();
		params.setMargins(30, 0, 30, 0); // substitute parameters for left, top,
											// right, bottom
		params.width = LinearLayout.LayoutParams.MATCH_PARENT;
		params.height = 180;
		tv.setLayoutParams(params);

		System.out.println(rl.getLayoutParams());
		
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

		tv.setText(" " + meal.getType() + ", " + meal.getDay() + "\n");
		tv.setClickable(false);

		tv.setTextSize(18);
		tv.setTextColor(Color.parseColor("#707070"));
		tv.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
		tv.setPadding(8, 0, 0, 0);

		tv.setBackgroundResource(com.app.bandeco.R.drawable.card_bg);
		rl.addView(tv);
		RelativeLayout.LayoutParams params = (LayoutParams) tv
				.getLayoutParams();
		params.setMargins(30, 0, 30, 0); // substitute parameters for left, top,
											// right, bottom
		params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
		tv.setLayoutParams(params);

		return rl;
	}

	public void setMain(Main main) {
		this.main = main;
	}

}
