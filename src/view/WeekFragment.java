package view;

import com.app.bandeco.Main;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WeekFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View week = inflater.inflate(com.app.bandeco.R.layout.week_layout, container, false);
		
		return week;
	}

	public void setMain(Main main) {
		// TODO Auto-generated method stub
		
	}
}
