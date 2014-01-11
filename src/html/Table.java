package html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Table implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1791808816952834003L;
	List<List<String>> table = new ArrayList<List<String>>();

	public String getElement(int row, int column) {
		return table.get(row).get(column);
	}

	public int rows() {
		return table.size();
	}

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

	public int searchForValueInRow(int row, String value) {
		List<String> l = table.get(row);
		for (int i = 0; i < l.size(); i++)
			if (l.get(i).toLowerCase().contains(value.toLowerCase()))
				return i;

		return -1;
	}

	public int searchValueInColumn(int column, String value) {
		for (int i = 0; i < table.size(); i++) {
			String s = table.get(i).get(column);
			if (s.toLowerCase().contains(value.toLowerCase()))
				return i;
		}
		return -1;
	}

	public List<String> getColumn(int column) {
		List<String> l = new ArrayList<String>();

		for (int i = 0; i < rows(); i++)
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
