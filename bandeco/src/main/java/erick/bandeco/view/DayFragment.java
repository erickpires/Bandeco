package erick.bandeco.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.bandeco.Main;
import com.app.bandeco.Utils;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

import java.util.Calendar;

import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;

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
		Day today = Main.week.getDayAt(Day.adaptDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));

		cardUI.addStack(new CardStack(""));

		createCards(today);

		cardUI.refresh();

		relativeLayout.addView(cardUI);
		return relativeLayout;
	}

	private void createCards(Day today) {
		Meal lunch = today.getLunch();
		Meal dinner = today.getDinner();
		Context context = getActivity().getApplicationContext();
		Card lunchCard = new Card(Utils.getMealType(lunch, context), Utils.getTextFromMeal(lunch, context), "#424242", "#0000e4", false);
		Card dinnerCard = new Card(Utils.getMealType(dinner, context), Utils.getTextFromMeal(dinner, context), "#424242", "#0000e4", false);

		cardUI.addCardToLastStack(dinnerCard);
		cardUI.addCardToLastStack(lunchCard);
	}

	public void update() {
	}
}
