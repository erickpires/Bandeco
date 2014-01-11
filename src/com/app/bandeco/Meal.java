package com.app.bandeco;

public class Meal {
	//Sorry, but this part will be written in Portuguese
	
	private String entrada;
	private String pratoPrincipal;
	private String vegetariana;
	private String guarnicao;
	private String acompanhamento;
	private String sobremesa;
	private String refresco;
	
	public Meal(String entrada, String pratoPrincipal, String vegetariana,
			String guarnicao, String acompanhamento, String sobremesa,
			String refresco) {
		this.entrada = entrada;
		this.pratoPrincipal = pratoPrincipal;
		this.vegetariana = vegetariana;
		this.guarnicao = guarnicao;
		this.acompanhamento = acompanhamento;
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

	public String getAcompanhamento() {
		return acompanhamento;
	}

	public String getSobremesa() {
		return sobremesa;
	}

	public String getRefresco() {
		return refresco;
	}
}
