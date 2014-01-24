package view;

import java.util.Calendar;

import model.Day;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.bandeco.Main;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

public class DayFragment extends Fragment {
	private CardUI cardUI;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(
				com.app.bandeco.R.layout.day_layout, container, false);
		
		
		Calendar calendar = Calendar.getInstance();
		
		relativeLayout.removeView(cardUI);
		cardUI = new CardUI(getActivity());
		Day today = Main.week.getDay(Day.adaptDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
		
		cardUI.addStack(new CardStack("" + today));
		today.generateCards(cardUI);
		
		cardUI.refresh();

		relativeLayout.addView(cardUI);		
		return relativeLayout;
	}

}
