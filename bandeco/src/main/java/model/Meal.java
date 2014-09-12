package model;

import java.util.List;

import view.Card;

import com.app.bandeco.ApplicationHelper;
import com.app.bandeco.Indexer;

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
	private Day day;

    public Meal(String mealType){
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

    public Meal(int mealType){
        this(mealTypeFromInt(mealType));
    }

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
		if(acomp.length > 1)
			feijao = acomp[1];
		else
			feijao = "";

		tmp = tableColumn.get(indexer.getIndexOfSobremesa());

		String[] sobremesaRefresco = tmp.split("/");

		sobremesa = sobremesaRefresco[0];
		if(sobremesaRefresco.length > 1)
			refresco = sobremesaRefresco[1];
		else
			refresco = "";

		formatText();

		acompanhamento = arroz;
		
		if(feijao.length() > 0)
			 acompanhamento += " e " + feijao;
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
		
		entrada = entrada.replace(";;", "");
		pratoPrincipal = pratoPrincipal.replace(";;", "");
		vegetariana = vegetariana.replace(";;", "");
		guarnicao = guarnicao.replace(";;", "");
		feijao = feijao.replace(";;", "");
		sobremesa = sobremesa.replace(";;", "");
		refresco = refresco.replace(";;", "");
		arroz = arroz.replace(";;", "");

		arroz = arroz.replace("/", "ou ");

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

	public Card createCard() {
		
		
		return new Card(type, this.toString(), "#424242", "#0000e4", false);
	}
	
	@Override
	public String toString(){
		String text = "";
		text += "Entrada: " + entrada + "\n";
		text += "Guarnição: " + guarnicao + "\n";
		text += "Prato Principal: " + pratoPrincipal + "\n";
		text += "Prato Vegetariano: " + vegetariana + "\n";
		text += "Acompanhamento: " + acompanhamento + "\n";
		text += "Sobremesa: " + sobremesa + "\n";
		text += "Refresco: " + refresco + "\n";
		
		return text;
	}

	public String getDay() {
		return "" + day;
	}

	public void setDay(Day day) {
		this.day = day;	
	}

    public static final String mealTypeFromInt(int mealType){
        switch (mealType){
            case ApplicationHelper.MEAL_TYPE_LUNCH:
                return "Almoço";
            case ApplicationHelper.MEAL_TYPE_DINNER:
                return "Jantar";
            default:
                return null;
        }
    }
}
