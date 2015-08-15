package erick.bandeco.model;

import static com.app.bandeco.Constants.MEAL_TYPE_DINNER;
import static com.app.bandeco.Constants.MEAL_TYPE_LUNCH;

public class Day {
	private Meal lunch;
	private Meal dinner;
	private int weekDay;
	private int monthDay;
	private int month;

	public Day(Meal lunch, Meal dinner, int weekDay) {
		this(lunch, dinner, weekDay, 0, 1);
	}

	public Day(Meal lunch, Meal dinner, int weekDay, int monthDay, int month) {
		this.lunch = lunch;
		this.dinner = dinner;
		this.weekDay = weekDay;
		this.monthDay = monthDay;
		this.month = month;

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

	public int getMonthDay() {
		return monthDay;
	}

	public int getMonth() {
		return month;
	}
}
