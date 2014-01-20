package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.app.bandeco.Day;
import com.app.bandeco.Meal;

public class MealCard extends TextView {

	private Day day;
	private Meal meal;
	
	public MealCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setMeal(Meal meal){
		this.meal = meal;
		updateText();
	}
	
	private void updateText() {
		String text = "";
		if(day != null)
			text += day;
		
		if(meal != null){
			text += "						" + meal.getType() + "\n\n";
			text += "Entrada: " + meal.getEntrada() + "\n";
			text += "Guarnição : " + meal.getGuarnicao() + "\n";
			text += "Prato Principal : " + meal.getPratoPrincipal() + "\n";
			text += "Op. Vegetariana : " + meal.getVegetariana() + "\n";
			text += "Acompanhamento :  " + meal.getAcompanhamento() + "\n";
			text += "Sobremesa :  " + meal.getSobremesa() + "\n";
			text += "Refresco: " + meal.getRefresco() + "\n";
		}
			
		super.setText(text);
	}

	public void setDay(Day day){
		this.day = day;
		updateText();
	}

}
