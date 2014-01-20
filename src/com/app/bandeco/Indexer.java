package com.app.bandeco;

import java.util.List;
import java.util.Locale;

public class Indexer {
	private int indexOfEntrada;
	private int indexOfPratoPrincipal;
	private int indexOfVegetariana;
	private int indexOfGuarnicao;
	private int indexOfAcompanhamento;
	private int indexOfSobremesa;
	private int indexOfRefresco;
	
	public Indexer (List<String> list){
		
		for(int i = 0; i < list.size(); i++){
			String field = list.get(i);
			field = field.toLowerCase();
			
			if(field.contains("entrada"))
				indexOfEntrada = i;
			if(field.contains("acompanhamento"))
				indexOfAcompanhamento = i;
			if(field.contains("prato principal"))
				indexOfPratoPrincipal = i;
			if(field.contains("vegetariana"))
				indexOfVegetariana = i;
			if(field.contains("guarnição"))
				indexOfGuarnicao = i;
			if(field.contains("sobremesa"))
				indexOfSobremesa = i;
			if(field.contains("refresco"))
				indexOfRefresco = i;
		}
	}
	
//	public void setIndexOfEntrada(int indexOfEntrada) {
//		this.indexOfEntrada = indexOfEntrada;
//	}
//	public void setIndexOfPratoPrincipal(int indexOfPratoPrincipal) {
//		this.indexOfPratoPrincipal = indexOfPratoPrincipal;
//	}
//	public void setIndexOfVegetariana(int indexOfVegetariana) {
//		this.indexOfVegetariana = indexOfVegetariana;
//	}
//	public void setIndexOfGuarnicao(int indexOfGuarnicao) {
//		this.indexOfGuarnicao = indexOfGuarnicao;
//	}
//	public void setIndexOfAcompanhamento(int indexOfAcompanhamento) {
//		this.indexOfAcompanhamento = indexOfAcompanhamento;
//	}
//	public void setIndexOfRefresco(int indexOfRefresco) {
//		this.indexOfRefresco = indexOfRefresco;
//	}
//	public void setIndexOfSobremesa(int indexOfSobremesa) {
//		this.indexOfSobremesa = indexOfSobremesa;
//	}
	
	public int getIndexOfEntrada() {
		return indexOfEntrada;
	}
	
	public int getIndexOfAcompanhamento() {
		return indexOfAcompanhamento;
	}
	
	public int getIndexOfGuarnicao() {
		return indexOfGuarnicao;
	}
	
	public int getIndexOfSobremesa() {
		return indexOfSobremesa;
	}
	
	public int getIndexOfPratoPrincipal() {
		return indexOfPratoPrincipal;
	}
	
	public int getIndexOfRefresco() {
		return indexOfRefresco;
	}
	
	public int getIndexOfVegetariana() {
		return indexOfVegetariana;
	}
	
	
}
