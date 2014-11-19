package erick.bandeco.view;

import erick.bandeco.adapters.MyExpandableListViewAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.bandeco.Main;

public class WeekFragment extends Fragment {

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ListView listView = (ListView) inflater.inflate(com.app.bandeco.R.layout.week_layout, container, false);
		
		listView.setPadding(0, 15, 0, 0);

        MyExpandableListViewAdapter adapter = new MyExpandableListViewAdapter(getActivity());
		
		for(int i = 0; i < 7; i++){
			adapter.add(2 * i, Main.week.getDayAt(i).getLunch());
			adapter.add(2 * i + 1, Main.week.getDayAt(i).getDinner());
		}
		
		adapter.setLimit(1);
		listView.setAdapter(adapter);

		
		return listView;
	}
}