package erick.bandeco.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.app.bandeco.R;
import com.app.bandeco.Utils;
import com.nhaarman.listviewanimations.util.Swappable;


public class MenuEntriesListAdapter extends BaseAdapter implements Swappable {
	final long[] magicVibrationPattern = {0, 12, 10, 12};

	Context context;
	private Integer[] menuEntriesOrder;
	String[] entries;
	Boolean[] checked;


	public MenuEntriesListAdapter(Context context, Integer[] menuEntriesOrder, Boolean[] checked) {
		this.context = context;
		this.menuEntriesOrder = menuEntriesOrder;
		this.checked = checked;

		entries = Utils.sortMenuEntries(context.getResources().getStringArray(R.array.menu_entries), menuEntriesOrder);
	}

	@Override
	public int getCount() {
		return entries.length;
	}

	@Override
	public Object getItem(int position) {
		return entries[position];
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.menu_entry_element, parent, false);
		}

		final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.menu_entry_checkbox);

		checkBox.setText(entries[position]);
		checkBox.setChecked(checked[position]);
		checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checked[position] = !checked[position];
			}
		});

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void swapItems(int positionOne, int positionTwo) {
		Utils.arraySwap(entries, positionOne, positionTwo);
		Utils.arraySwap(menuEntriesOrder, positionOne, positionTwo);
		Utils.arraySwap(checked, positionOne, positionTwo);

		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(magicVibrationPattern, -1);
	}
}
