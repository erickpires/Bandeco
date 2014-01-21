package com.app.bandeco;

import java.util.List;

import view.Card;

public class Meal {
	// Sorry, but this part will be written in Portuguese

	private String entrada;
	private String pratoPrincipal;
	private String vegetariana;
	private String guarnicao;
	private String feijao;
	private String arroz;
	private String sobremesa;
	private String refresco;
	private String acompanhamento;
	private String type;

	public Meal(List<String> tableColumn, Indexer indexer, String type) {
		String tmp;

		this.type = type;

		entrada = tableColumn.get(indexer.getIndexOfEntrada());
		pratoPrincipal = tableColumn.get(indexer.getIndexOfPratoPrincipal());
		vegetariana = tableColumn.get(indexer.getIndexOfVegetariana());
		guarnicao = tableColumn.get(indexer.getIndexOfGuarnicao());

		tmp = tableColumn.get(indexer.getIndexOfAcompanhamento());

		String[] acomp = tmp.split(";;");

		arroz = acomp[0];
		feijao = acomp[1];

		tmp = tableColumn.get(indexer.getIndexOfSobremesa());

		String[] sobremesaRefresco = tmp.split("/");

		sobremesa = sobremesaRefresco[0];
		refresco = sobremesaRefresco[1];

		formatText();

		acompanhamento = arroz + " e " + feijao;
	}

	// public Meal(String entrada, String pratoPrincipal, String vegetariana,
	// String guarnicao, String feijao, String arroz, String sobremesa,
	// String refresco) {
	// this.entrada = entrada;
	// this.pratoPrincipal = pratoPrincipal;
	// this.vegetariana = vegetariana;
	// this.guarnicao = guarnicao;
	// this.feijao = feijao;
	// this.arroz = arroz;
	// this.sobremesa = sobremesa;
	// this.refresco = refresco;
	// }

	private void formatText() {
		entrada = entrada.trim();
		pratoPrincipal = pratoPrincipal.trim();
		vegetariana = vegetariana.trim();
		guarnicao = guarnicao.trim();
		feijao = feijao.trim();
		sobremesa = sobremesa.trim();
		refresco = refresco.trim();
		arroz = arroz.trim();

		arroz = arroz.replace("/", "ou");

	}

	public String getEntrada() {
		return entrada;
	}

	public String getPratoPrincipal() {
		return pratoPrincipal;
	}

	public String getVegetariana() {
		return vegetariana;
	}

	public String getGuarnicao() {
		return guarnicao;
	}

	public String getFeijao() {
		return feijao;
	}

	public String getArroz() {
		return arroz;
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

	public String getType() {
		return type;
	}

	public Card createCard() {
		String text = "";
		text += "Entrada: " + entrada + "\n";
		text += "Guarnição : " + guarnicao + "\n";
		text += "Prato Principal : " + pratoPrincipal + "\n";
		text += "Op. Vegetariana : " + vegetariana + "\n";
		text += "Acompanhamento :  " + acompanhamento + "\n";
		text += "Sobremesa :  " + sobremesa + "\n";
		text += "Refresco: " + refresco + "\n";
		
		return new Card(type, text, "#000000", "#0000e4", false);
	}
}
