package com.app.bandeco;

public class Day {
	private Meal lunch;
	private Meal dinner;
	private int weekDay;

	public Day(Meal lunch, Meal dinner, int weekDay) {
		this.lunch = lunch;
		this.dinner = dinner;
		this.weekDay = weekDay;
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

	private static String weekDayToString(int weekDay) {
		switch (weekDay) {
		case 0:
			return "Segunda";
		case 1:
			return "Terça";
		case 2:
			return "Quarta";
		case 3:
			return "Quinta";
		case 4:
			return "Sexta";
		case 5:
			return "Sábado";
		case 6:
			return "Domingo";
		default:
			return null;
		}
	}
}
