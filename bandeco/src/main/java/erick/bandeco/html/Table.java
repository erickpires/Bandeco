package erick.bandeco.html;

import java.util.ArrayList;
import java.util.List;

public class Table {

	/**
	 *
	 */
	private List<List<String>> table = new ArrayList<>();

	public String getElement(int row, int column) {
		return table.get(row).get(column);
	}

	@SuppressWarnings("WeakerAccess")
	public int rows() {
		return table.size();
	}

	@SuppressWarnings("WeakerAccess")
	public int columns(int row) {
		return table.get(row).size();
	}

	public void add(int row, int column, String value) {
		int numRows = rows();

		while (numRows <= row) {
			table.add(new ArrayList<String>());
			numRows++;
		}

		table.get(row).add(column, value);
	}

	public void add(int row, String value) {
		int numRows = rows();

		while (numRows <= row) {
			table.add(new ArrayList<String>());
			numRows++;
		}

		table.get(row).add(value);
	}

	public void addRow(List<String> row) {
		table.add(row);
	}

	@SuppressWarnings("SameParameterValue")
	public int searchValueInColumn(int column, String value) {
		for (int i = 0; i < table.size(); i++) {
			String s = table.get(i).get(column);
			if (s.toLowerCase().contains(value.toLowerCase()))
				return i;
		}
		return -1;
	}

	public List<String> getColumn(int column, int start) {
		List<String> l = new ArrayList<>();

		for (int i = start; i < rows(); i++)
			if (columns(i) > column)
				l.add(getElement(i, column));

		return l;
	}

	@Override
	public String toString() {
		String tmp = "";

		for (List<String> l : table) {
			for (String s : l)
				tmp += s + "	";

			tmp += "\n";
		}

		return tmp;
	}

	public void trimCells() {
		for(List<String> row : table) {
			for(int i = 0; i < row.size(); i++) {
				row.set(i, row.get(i).trim());
			}
		}
	}

	public void removeBlankLines() {
		for (int row = 0; row < table.size(); ) {
			List<String> line = table.get(row);
			boolean shouldRemoveRow = true;

			for (int column = 0; column < line.size(); column++) {
				if(!line.get(column).equals("")) {
					shouldRemoveRow = false;
					break;
				}
			}

			if(shouldRemoveRow)
				table.remove(row);
			else
				row++;
		}
	}

	public List<Table> split(String search) {
		List<Table> result = new ArrayList<>();
		Table currentTable = new Table();
		result.add(currentTable);

		for(List<String> row : table) {
			if (row.contains(search)) {
				currentTable = new Table();
				result.add(currentTable);
			}

			currentTable.addRow(row);
		}

		return result;
	}

	public String getLineContainingIgnoreCase(String search) {
		String searchToLower = search.toLowerCase();
		for(List<String> row : table) {
			if(rowContainsIgnoreCase(row, searchToLower)) {
				String result = "";
				for(String s : row) {
					result += s + " ";
				}
				return result;
			}
		}

		return null;
	}

	private static boolean rowContainsIgnoreCase(List<String> row, String search) {
		for(String s : row) {
			if (s.toLowerCase().contains(search))
				return true;
		}

		return false;
	}
}
