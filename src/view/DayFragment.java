package view;

import java.util.Calendar;
import java.util.Date;

import model.Day;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.app.bandeco.Main;
import com.app.bandeco.R;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

public class DayFragment extends Fragment {

	private Main main;
	private CardUI cardUI;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(
				com.app.bandeco.R.layout.day_layout, container, false);
		
		
		Calendar calendar = Calendar.getInstance();
		
		relativeLayout.removeView(cardUI);
		cardUI = new CardUI(getActivity());
		Day today = main.getWeek().getDay(Day.adaptDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
		//
		cardUI.addStack(new CardStack("" + today));
		today.generateCards(cardUI);
		// cardUI.addCardToLastStack(new Card("titulo", "corpo",
		// "#0000", "#0000", false));
		//
		cardUI.refresh();

		relativeLayout.addView(cardUI);

//		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cardUI
//				.getLayoutParams();
//		params.addRule(RelativeLayout.BELOW, R.id.day_spinner);
//		cardUI.setLayoutParams(params);
		
		return relativeLayout;
	}

	public void setMain(Main main) {
		this.main = main;
	}

}
