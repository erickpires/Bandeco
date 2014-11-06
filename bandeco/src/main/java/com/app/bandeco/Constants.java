package com.app.bandeco;

import java.text.SimpleDateFormat;

import erick.bandeco.database.DatabaseContract;

public abstract class Constants {
    public static final String SITE_URL_OFICIAL = "http://www.nutricao.ufrj.br/cardapio.htm";
    public static final String SITE_URL_BACKUP = "http://dcc.ufrj.br/~erickpires/cardapio1.htm";
    public static String SITE_URL = SITE_URL_OFICIAL;

    public static final String BREAK_LINE = ";;";
    public static final String SOBREMESA_REFRESCO_SEPARATOR = "/";

    public static final int DAYS_IN_THE_WEEK = 7;

    public static final int MEAL_TYPE_LUNCH = 0;
    public static final int MEAL_TYPE_DINNER = 1;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String[] mealProjection = new String[]{DatabaseContract.Meals.MEAL_TYPE,
            DatabaseContract.Meals.DAY,
            DatabaseContract.Meals.ENTRADA,
            DatabaseContract.Meals.GUARNICAO,
            DatabaseContract.Meals.PRATO_PRINCIPAL,
            DatabaseContract.Meals.PRATO_VEGETARIANO,
            DatabaseContract.Meals.ACOMPANHAMENTO,
            DatabaseContract.Meals.SOBREMESA,
            DatabaseContract.Meals.REFRESCO
    };
}
