package erick.bandeco.html;

import com.app.bandeco.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Html {

	enum Estado {
		ignorando, lendoTabela, lendoLinha, lendoCampo
	}

	private List<Table> tables = new ArrayList<Table>();

	public Html(URLConnection connection) throws IOException {

		InputStreamReader reader = new InputStreamReader(connection.getInputStream());

		createTables(reader);
	}

	private void createTables(InputStreamReader reader) throws IOException {
		BufferedReader in = new BufferedReader(reader);
		String inputLine;
		Estado estado = Estado.ignorando;

		String tmp = "";
		int currentRow = 0;
		int currentTable = 0;

		while ((inputLine = in.readLine()) != null) {

			if (inputLine.contains("<table") && estado == Estado.ignorando) {
				estado = Estado.lendoTabela;
				tables.add(new Table());
			}

			if (inputLine.contains("<tr") && estado == Estado.lendoTabela) {
				estado = Estado.lendoLinha;
			}

			if (inputLine.contains("<td") && estado == Estado.lendoLinha) {
				estado = Estado.lendoCampo;
			}

			if (estado == Estado.lendoCampo && !inputLine.contains("</td")) {
				tmp += inputLine;
			}
			if (estado == Estado.lendoCampo && inputLine.contains("</td")) {
				estado = Estado.lendoLinha;

				tmp += inputLine;

				String data = getDataFromLine(tmp);

				tables.get(currentTable).add(currentRow, data);

				tmp = "";
			}

			if (inputLine.contains("</tr") && estado == Estado.lendoLinha) {
				estado = Estado.lendoTabela;

				currentRow++;
			}

			if (inputLine.contains("</table") && estado == Estado.lendoTabela) {
				estado = Estado.ignorando;

				currentTable++;
				currentRow = 0;
			}
		}
	}

	public List<Table> getTables() {
		return tables;
	}

	private static String getDataFromLine(String str) {
		str = str.replaceAll("<br />", Constants.BREAK_LINE);
		str = str.replaceAll("<[^>]*>", "");
		str = str.replaceAll("&nbsp;", "");
		str = str.replaceAll("[\t]+", " ");
		str = str.replaceAll("[ ]+", " ");
		str = str.replaceAll(" [ ]+", " ");
		str = str.replaceAll("(c|C)( )?/", "com");

		return str;
	}
}
