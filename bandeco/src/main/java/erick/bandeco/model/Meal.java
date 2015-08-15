package erick.bandeco.model;

import com.app.bandeco.Constants;

import java.util.List;

import erick.bandeco.html.Indexer;

import static com.app.bandeco.Constants.BREAK_LINE;

public class Meal {

	private String entrada;
	private String pratoPrincipal;
	private String vegetariana;
	private String guarnicao;
	private String feijao;
	private String arroz;
	private String sobremesa;
	private String refresco;
	private String acompanhamento;
	private int type;
	private Day day;

	public Meal(int mealType) {
		this.type = mealType;

		// Ensures that the important fields are filled
		entrada = "";
		pratoPrincipal = "";
		vegetariana = "";
		guarnicao = "";
		sobremesa = "";
		refresco = "";
		acompanhamento = "";
	}

	public Meal(List<String> tableColumn, Indexer indexer, int type) {
		String tmp;

		this.type = type;

		entrada = tableColumn.get(indexer.getIndexOfEntrada());
		pratoPrincipal = tableColumn.get(indexer.getIndexOfPratoPrincipal());
		vegetariana = tableColumn.get(indexer.getIndexOfVegetariana());
		guarnicao = tableColumn.get(indexer.getIndexOfGuarnicao());

		tmp = tableColumn.get(indexer.getIndexOfAcompanhamento());

		String[] acomp = tmp.split(BREAK_LINE);

		arroz = acomp[0];
		if (acomp.length > 1)
			feijao = acomp[1];
		else
			feijao = "";

		tmp = tableColumn.get(indexer.getIndexOfSobremesa());

		if (indexer.getIndexOfRefresco() == indexer.getIndexOfSobremesa()) {

			String[] sobremesaRefresco = tmp.split(Constants.SOBREMESA_REFRESCO_SEPARATOR);

			sobremesa = sobremesaRefresco[0];
			if (sobremesaRefresco.length > 1)
				refresco = sobremesaRefresco[1];
			else
				refresco = "";
		} else
			refresco = tableColumn.get(indexer.getIndexOfRefresco());


		//formatText();

		acompanhamento = arroz;

		if (feijao.length() > 0)
			acompanhamento += " e " + feijao;
	}

	private void formatText() {
		entrada = entrada.trim();
		pratoPrincipal = pratoPrincipal.trim();
		vegetariana = vegetariana.trim();
		guarnicao = guarnicao.trim();
		feijao = feijao.trim();
		sobremesa = sobremesa.trim();
		refresco = refresco.trim();
		arroz = arroz.trim();

		entrada = entrada.replace(BREAK_LINE, "");
		pratoPrincipal = pratoPrincipal.replace(BREAK_LINE, "");
		vegetariana = vegetariana.replace(BREAK_LINE, "");
		guarnicao = guarnicao.replace(BREAK_LINE, "");
		feijao = feijao.replace(BREAK_LINE, "");
		sobremesa = sobremesa.replace(BREAK_LINE, "");
		refresco = refresco.replace(BREAK_LINE, "");
		arroz = arroz.replace(BREAK_LINE, "");

		arroz = arroz.replaceAll(" */ *", " ou ");

	}

	public String getEntrada() {
		return entrada;
	}

	public String getPratoPrincipal() {
		return pratoPrincipal;
	}

	public String getPratoVegetariano() {
		return vegetariana;
	}

	public String getGuarnicao() {
		return guarnicao;
	}

	public String getSobremesa() {
		return sobremesa;
	}

	public String getRefresco() {
		return refresco;
	}

	public String getAcompanhamento() {
		return acompanhamento;
	}

	public int getType() {
		return type;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public void setPratoPrincipal(String pratoPrincipal) {
		this.pratoPrincipal = pratoPrincipal;
	}

	public void setPratoVegetariano(String vegetariana) {
		this.vegetariana = vegetariana;
	}

	public void setGuarnicao(String guarnicao) {
		this.guarnicao = guarnicao;
	}

	public void setSobremesa(String sobremesa) {
		this.sobremesa = sobremesa;
	}

	public void setRefresco(String refresco) {
		this.refresco = refresco;
	}

	public void setAcompanhamento(String acompanhamento) {
		this.acompanhamento = acompanhamento;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}
}
