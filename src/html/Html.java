package html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Html implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5454010562532372571L;

	enum Estado {
		ignorando, lendoTabela, lendoLinha, lendoCampo
	};

	private List<Table> tables = new ArrayList<Table>();
	private long lastModifiedDate;
	//private InputStreamReader reader;

//	public Html(URL url) throws IOException {
//		URLConnection connection = url.openConnection();
//		connection.connect();
//		//reader = new InputStreamReader(connection.getInputStream());
//
//		//createTables();
//	}
	public Html(URLConnection connection) throws IOException{
		lastModifiedDate = connection.getLastModified();
		
		InputStreamReader reader = new InputStreamReader(connection.getInputStream());
		
		createTables(reader);
	}

	private void createTables(InputStreamReader reader) throws IOException {
		BufferedReader in = new BufferedReader(reader);
		String inputLine;
		Estado estado = Estado.ignorando;

		String tmp = "";
		int currentRow = 0;
		int currentColumn = 0;
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
				currentColumn++;
			}

			if (inputLine.contains("</tr") && estado == Estado.lendoLinha) {
				estado = Estado.lendoTabela;

				currentRow++;
				currentColumn = 0;
			}

			if (inputLine.contains("</table") && estado == Estado.lendoTabela) {
				estado = Estado.ignorando;

				currentTable++;
				currentRow = 0;
				currentColumn = 0;
			}
		}
	}
	
	public List<Table> getTables(){
		return tables;
	}

	private static String getDataFromLine(String str) {
		str = str.replaceAll("<br />", ";;");
		str = str.replaceAll("<[[/]a-zA-Z=\"0-9#:.! -]*>", "");
		str = str.replaceAll("&nbsp;", "");
		str = str.replaceAll(" [ ]*", " ");

		return str;
	}
}
