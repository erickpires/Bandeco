package com.app.bandeco;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import erick.bandeco.model.Meal;
import erick.bandeco.model.Week;

import static erick.bandeco.database.DatabaseContract.*;


public abstract class OperationsWithDB {

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

    public static void updateLastModifiedInDatabase(SQLiteDatabase db, Date date){
        ContentValues values = new ContentValues();

        values.put(LastUpdate.TABLE_ID, 0);
        values.put(LastUpdate.DATE, Constants.dateFormat.format(date));

        db.insertWithOnConflict(LastUpdate.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static Meal getMealFromDatabase(SQLiteDatabase database, int dayOfTheWeek, int mealType) {
        Meal meal = new Meal(mealType);

        String selection = Meals.MEAL_TYPE + "=? AND " + Meals.DAY + "=?";

        String[] selectionValues = {String.valueOf(mealType),
                                    String.valueOf(dayOfTheWeek)};

        Cursor cursor = database.query(Meals.TABLE_NAME, Constants.mealProjection, selection, selectionValues, null, null, null);

        if(cursor.moveToFirst()){
            setMealWithCursor(meal, cursor);
        }

        return meal;
    }

    private static void setMealWithCursor(Meal meal, Cursor cursor) {
        meal.setEntrada(cursor.getString(cursor.getColumnIndex(Meals.ENTRADA)));
        meal.setGuarnicao(cursor.getString(cursor.getColumnIndex(Meals.GUARNICAO)));
        meal.setPratoPrincipal(cursor.getString(cursor.getColumnIndex(Meals.PRATO_PRINCIPAL)));
        meal.setPratoVegetariano(cursor.getString(cursor.getColumnIndex(Meals.PRATO_VEGETARIANO)));
        meal.setAcompanhamento(cursor.getString(cursor.getColumnIndex(Meals.ACOMPANHAMENTO)));
        meal.setSobremesa(cursor.getString(cursor.getColumnIndex(Meals.SOBREMESA)));
        meal.setRefresco(cursor.getString(cursor.getColumnIndex(Meals.REFRESCO)));
    }

    public static Week getWeekFromDatabase(SQLiteDatabase database) {
        Week week = Week.createEmptyWeek();

        Cursor cursor = database.query(Meals.TABLE_NAME, Constants.mealProjection, null, null, null, null, null);

        if(cursor.moveToFirst())
            do{
                int mealType = cursor.getInt(cursor.getColumnIndex(Meals.MEAL_TYPE));
                int day = cursor.getInt(cursor.getColumnIndex(Meals.DAY));

                Meal meal = week.getDayAt(day).getMeal(mealType);
                setMealWithCursor(meal, cursor);

            }while (cursor.moveToNext());

        return week;
    }

    public static void saveListToDB(SQLiteDatabase database, ArrayList<String> list, String table, String column) {
        //Cleans table before insert elements
        database.delete(table, null, null);

        for (String s : list) {
            ContentValues values = new ContentValues();
            values.put(column, s);

            database.insertOrThrow(table, null, values);
        }
    }

    public static ArrayList<String> getListFromDB(SQLiteDatabase database, String table, String[] projection) {
        ArrayList<String> list = new ArrayList<String>();

        Cursor cursor = database.query(table, projection, null, null, null, null, null);

        int column = cursor.getColumnIndex(projection[0]);

        if (cursor.moveToFirst())
            do
                list.add(cursor.getString(column));

             while (cursor.moveToNext());

        return list;
    }
}
