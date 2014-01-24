package view;

import java.util.Calendar;

import model.Week;
import adapters.MyExpandableListViewAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.bandeco.Main;

public class WeekFragment extends Fragment {

	private Main main;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ListView listView = (ListView) inflater.inflate(com.app.bandeco.R.layout.week_layout, container, false);
		
		
		Calendar calendar = Calendar.getInstance();
		
		MyExpandableListViewAdapter adapter = new MyExpandableListViewAdapter(getActivity());
		Week week = main.getWeek();
		
		for(int i = 0; i < 7; i++){
			adapter.add(week.getDay(i).getLunch());
			adapter.add(week.getDay(i).getDinner());
		}
//		adapter.add(null);
//		adapter.add(null);
		adapter.setLimit(1);
		listView.setAdapter(adapter);
		
		
		
		
		
		return listView;
	}

	public void setMain(Main main) {
		this.main = main;
		
	}
}
