package adapters;

import view.DayFragment;
import view.WeekFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.bandeco.Main;

public class TabsAdapter extends FragmentPagerAdapter {

	DayFragment DayFragment = new DayFragment();
	WeekFragment weekFragment = new WeekFragment();
	
	public TabsAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return DayFragment;
		case 1:
			return weekFragment;
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

	public void setMain(Main main) {
		DayFragment.setMain(main);
		weekFragment.setMain(main);
		
	}

}
