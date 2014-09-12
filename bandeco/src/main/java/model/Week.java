package model;

import html.Table;
import java.util.List;

import com.app.bandeco.Indexer;

public class Week {

	private Day[] days = new Day[7];
	
	private Table lunchTable;
	private Table dinnerTable;
	private Indexer lunchIndexer;
	private Indexer dinnerIndexer;
	private static final int startingRow = 1;
    private static final int columnsOffset = 1;

    public Week(List<Table> tables){
		for (int i = 0; i < tables.size(); i++)
			if (tables.get(i).searchValueInColumn(0, "Acompanhamento") != -1){
				if(tables.get(i).searchValueInColumn(0, "Almoço") != -1)
					lunchTable = tables.get(i);
				
				if(tables.get(i).searchValueInColumn(0, "Jantar") != -1)
					dinnerTable = tables.get(i);
			}
		
		if(lunchTable == null || dinnerTable == null)
			throw new RuntimeException("Couldn't find tables");
		
		createIndexers();
		
		createDays();
	}

	private void createDays() {
		for(int i = 0; i < 7; i++){
			Meal lunch = new Meal(lunchTable.getColumn(i + columnsOffset, startingRow), lunchIndexer, "Almoço");
			Meal dinner = new Meal(dinnerTable.getColumn(i + columnsOffset, startingRow), dinnerIndexer, "Jantar");
			
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

	public Day getDay(int index) {
		return days[index];
	}
}
