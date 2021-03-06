package erick.bandeco.model;

import com.app.bandeco.Constants;

import java.util.Calendar;
import java.util.List;

import erick.bandeco.html.Indexer;
import erick.bandeco.html.Table;

public class Week {

	private Day[] days = new Day[7];

	private Table lunchTable;
	private Table dinnerTable;
	private Indexer lunchIndexer;
	private Indexer dinnerIndexer;
	private static final int startingRow = 1;
	private static final int columnsOffset = 1;

	private Week() {

	}

	public Week(List<Table> tables) {
		for (Table table : tables)
			if (table.searchValueInColumn(0, "Acompanhamento") != -1) {
				if (table.searchValueInColumn(0, "Almoço") != -1)
					lunchTable = table;

				if (table.searchValueInColumn(0, "Jantar") != -1)
					dinnerTable = table;
			}

		if (lunchTable == null || dinnerTable == null)
			throw new RuntimeException("Couldn't find tables");

		createIndexers();

		createWeek();
	}

	private void createWeek() {
		for (int i = 0; i < 7; i++) {
			Meal lunch = new Meal(lunchTable.getColumn(i + columnsOffset, startingRow), lunchIndexer, Constants.MEAL_TYPE_LUNCH);
			Meal dinner = new Meal(dinnerTable.getColumn(i + columnsOffset, startingRow), dinnerIndexer, Constants.MEAL_TYPE_DINNER);

			days[i] = new Day(lunch, dinner, i);
		}
	}

	private void createIndexers() {
		//TODO find a proper name for tmp
		List<String> tmp = lunchTable.getColumn(0, startingRow);
		lunchIndexer = new Indexer(tmp);

		tmp = dinnerTable.getColumn(0, startingRow);
		dinnerIndexer = new Indexer(tmp);
	}

	public Day getDayAt(int index) {
		return days[index];
	}

	public Day getToday(){
		Calendar calendar = Calendar.getInstance();
		return getDayAt(Day.adaptDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
	}

	public static Week createEmptyWeek() {
		Week week = new Week();

		for (int i = 0; i < week.days.length; i++) {
			Meal lunch = new Meal(Constants.MEAL_TYPE_LUNCH);
			Meal dinner = new Meal(Constants.MEAL_TYPE_DINNER);

			week.days[i] = new Day(lunch, dinner, i);
			lunch.setDay(week.days[i]);
			dinner.setDay(week.days[i]);
		}
		return week;
	}
}
