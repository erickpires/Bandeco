package com.app.bandeco;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import model.Meal;

import static database.DatabaseContract.*;

/**
 * Created by erick on 9/12/14.
 */
public abstract class ApplicationHelper {
    public static final String url = "http://www.nutricao.ufrj.br/cardapio.htm";
    //public static final String url = "http://dcc.ufrj.br/~erickpires/cardapio1.htm";
    public static final int DAYS_IN_THE_WEEK = 7;

    public static final int MEAL_TYPE_LUNCH = 0;
    public static final int MEAL_TYPE_DINNER = 1;

    public static void insertMealInDatabase(SQLiteDatabase db, Meal meal, int day, int mealType){
        ContentValues values = new ContentValues();

        values.put(Meals.MEAL_TYPE, mealType);
        values.put(Meals.DAY, day);
        values.put(Meals.ENTRADA, meal.getEntrada());
        values.put(Meals.GUARNICAO, meal.getGuarnicao());
        values.put(Meals.PRATO_PRINCIPAL, meal.getPratoPrincipal());
        values.put(Meals.PRATO_VEGETARIANO, meal.getPratoVegetariano());
        values.put(Meals.ACOMPANHAMENTO, meal.getAcompanhamento());
        values.put(Meals.SOBREMESA, meal.getSobremesa());
        values.put(Meals.REFRESCO, meal.getRefresco());

        db.insertWithOnConflict(Meals.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
