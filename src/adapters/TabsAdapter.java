package adapters;

import view.DayFragment;
import view.WeekFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.bandeco.Main;

public class TabsAdapter extends FragmentPagerAdapter {

	DayFragment dayFragment;
	WeekFragment weekFragment;
	
	public TabsAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		dayFragment = new DayFragment();
		weekFragment = new WeekFragment();
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return dayFragment;
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
		dayFragment.setMain(main);
		weekFragment.setMain(main);
		
	}

}
