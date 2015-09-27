package erick.bandeco.model;

import java.util.Calendar;

import static com.app.bandeco.Constants.MEAL_TYPE_DINNER;
import static com.app.bandeco.Constants.MEAL_TYPE_LUNCH;

public class Day {
	private Meal lunch;
	private Meal dinner;
	private int weekDay;
	private Calendar date;

	public Day(Meal lunch, Meal dinner, int weekDay) {
		this(lunch, dinner, weekDay, null);
	}

	public Day(Meal lunch, Meal dinner, int weekDay, Calendar date) {
		this.lunch = lunch;
		this.dinner = dinner;
		this.weekDay = weekDay;
		this.date = date;

		lunch.setDay(this);
		dinner.setDay(this);
	}

	public Meal getLunch() {
		return lunch;
	}

	public Meal getDinner() {
		return dinner;
	}

	public static int adaptDayOfWeek(int day) {
		return (day + 5) % 7;
	}

	public Meal getMeal(int mealType) {
		switch (mealType) {
			case MEAL_TYPE_LUNCH:
				return lunch;
			case MEAL_TYPE_DINNER:
				return dinner;
			default:
				return null;
		}
	}

	public int getWeekDay() {
		return weekDay;
	}

	public Calendar getDate() {
		return date;
	}

	public long getDateMilliseconds() {
		return date.getTimeInMillis();
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
}
