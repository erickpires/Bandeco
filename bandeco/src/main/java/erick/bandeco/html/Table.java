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
}
