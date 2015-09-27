package erick.bandeco.html;

import com.app.bandeco.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Html {

	private List<Table> tables = new ArrayList<>();
	private Date lastModified;

	public Html(URLConnection connection) throws IOException {

		Document doc = Jsoup.parse(connection.getInputStream(), connection.getContentEncoding(), connection.getURL().getPath());

		lastModified = new Date(connection.getLastModified());

		createTables(doc);
	}

	private void createTables(Document doc) {

		for (Element table : doc.select("table")) {
			Table currentTable = new Table();
			int currentRow = 0;

			for (Element row : table.select("tr")) {
				if(!row.parent().parent().equals(table))
					continue;

				Elements tds = row.select("td");
				for(Element data : tds) {

					if(!data.parent().parent().parent().equals(table))
						continue;

					currentTable.add(currentRow, getDataFromLine("" + data));
				}
				currentRow++;
			}

			tables.add(currentTable);
		}
	}

	public List<Table> getTables() {
		return tables;
	}

	private static String getDataFromLine(String str) {
		str = str.replaceAll("<br>", Constants.BREAK_LINE);
		str = str.replaceAll("<[^>]*>", "");
		str = str.replaceAll("&nbsp;", "");
		str = str.replaceAll("[\t]+", " ");
		str = str.replaceAll("[\n]+", " ");
		str = str.replaceAll("[ ]+", " ");//TODO
		str = str.replaceAll(" [ ]+", " ");
		str = str.replaceAll("(c|C)( )?/", "com");

		return str;
	}

	public Date getLastModified() {
		return lastModified;
	}
}
