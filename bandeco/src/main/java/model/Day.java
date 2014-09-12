package model;

import view.Card;

import com.app.bandeco.ApplicationHelper;
import com.fima.cardsui.views.CardUI;

import static com.app.bandeco.ApplicationHelper.*;

public class Day {
	private Meal lunch;
	private Meal dinner;
	private int weekDay;

	public Day(Meal lunch, Meal dinner, int weekDay) {
		this.lunch = lunch;
		this.dinner = dinner;
		this.weekDay = weekDay;
		
		lunch.setDay(this);
		dinner.setDay(this);
	}

	public Meal getLunch() {
		return lunch;
	}

	public Meal getDinner() {
		return dinner;
	}

	@Override
	public String toString() {
		return weekDayToString(weekDay);
	}

    // Monday is considered the first day of the week
    // this way I can keep the site pattern
	private static String weekDayToString(int weekDay) {
		switch (weekDay) {
		case 0:
			return "Segunda-feira";
		case 1:
			return "Terça-feira";
		case 2:
			return "Quarta-feira";
		case 3:
			return "Quinta-feira";
		case 4:
			return "Sexta-feira";
		case 5:
			return "Sábado";
		case 6:
			return "Domingo";
		default:
			return null;
		}
	}

	public void generateCards(CardUI cardUI) {
		Card lunchCard = lunch.createCard();
		Card dinnerCard = dinner.createCard();
		
		cardUI.addCardToLastStack(dinnerCard);
		cardUI.addCardToLastStack(lunchCard);
		
	}

	public static int adaptDayOfWeek(int day) {
		return (day + 5) % 7;
	}

    public Meal getMeal(int mealType) {
        switch (mealType){
            case MEAL_TYPE_LUNCH:
                return lunch;
            case MEAL_TYPE_DINNER:
                return dinner;
            default:
                return null;
        }
    }
}
