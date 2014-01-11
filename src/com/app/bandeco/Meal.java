package com.app.bandeco;

public class Meal {
	//Sorry, but this part will be written in Portuguese
	
	private String entrada;
	private String pratoPrincipal;
	private String vegetariana;
	private String guarnicao;
	private String feijao;
	private String arroz;
	private String sobremesa;
	private String refresco;
	
	public Meal(String entrada, String pratoPrincipal, String vegetariana,
			String guarnicao, String feijao, String arroz, String sobremesa,
			String refresco) {
		this.entrada = entrada;
		this.pratoPrincipal = pratoPrincipal;
		this.vegetariana = vegetariana;
		this.guarnicao = guarnicao;
		this.feijao = feijao;
		this.arroz = arroz;
		this.sobremesa = sobremesa;
		this.refresco = refresco;
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
}
