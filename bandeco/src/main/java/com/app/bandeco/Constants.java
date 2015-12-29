package com.app.bandeco;

import erick.bandeco.database.DatabaseContract;

public final class Constants {
	public static final String SITE_URL_OFICIAL = "https://docs.google.com/spreadsheets/d/1YvCqBrNw5l4EFNplmpRBFrFJpjl4EALlVNDk3pwp_dQ/pubhtml";
	public static final String SITE_URL_BACKUP = "https://docs.google.com/spreadsheets/d/1Zpm1Qz3mIzob5hAYYq29_FbYT15j0damxZSWFBGyk0M/pubhtml";
	public static final String RECESS = "RECESS".toLowerCase();
	public static String SITE_URL = SITE_URL_OFICIAL;

	public static final String BREAK_LINE = ";;";
	public static final String SOBREMESA_REFRESCO_SEPARATOR = "/";

	public static final int DAYS_IN_THE_WEEK = 7;

	public static final int MEAL_TYPE_LUNCH = 0;
	public static final int MEAL_TYPE_DINNER = 1;

	// Meals to be displayed
	public static final int MEAL_OPTION_LUNCH_ONLY = 0;
	public static final int MEAL_OPTION_DINNER_ONLY = 1;
	public static final int MEAL_OPTION_BOTH_MEALS = 2;

	public static final int[] DAYS_TO_NOTIFY_CODES = new int[]{1, 2, 4, 8, 16, 32, 64};
	public static final int DEFAULT_DAYS_TO_NOTIFY_CODE = 1 | 2 | 4 | 8 | 16;

	public static final int CODE_OF_PRATO_PRINCIPAL = 0;
	public static final int CODE_OF_PRATO_VEGETARIANO = 1;
	public static final int CODE_OF_ACOMPANHAMENTO = 2;
	public static final int CODE_OF_GUARNICAO = 3;
	public static final int CODE_OF_ENTRADA = 4;
	public static final int CODE_OF_SOBREMESA = 5;
	public static final int CODE_OF_REFRESCO = 6;

	public static final int ALL_MENU_ENTRIES_CODE = 0x7F;
	public static final int DEFAULT_MENU_ENTRIES_CODE = 0x1AC688;

	public static final String[] mealProjection = new String[]{
		DatabaseContract.Meals.MEAL_TYPE,
	    DatabaseContract.Meals.DAY,
		DatabaseContract.Meals.IS_RECESS,
	    DatabaseContract.Meals.ENTRADA,
	    DatabaseContract.Meals.GUARNICAO,
	    DatabaseContract.Meals.PRATO_PRINCIPAL,
	    DatabaseContract.Meals.PRATO_VEGETARIANO,
	    DatabaseContract.Meals.ACOMPANHAMENTO,
	    DatabaseContract.Meals.SOBREMESA,
	    DatabaseContract.Meals.REFRESCO
	};
	public static final String ASSETS_FOLDER = "file:///android_assets/";

	public static final int COLLAPSED_MAX_LINES = 3;
	public static final int HEIGHT_ANIMATION_DURATION = 100;

	private Constants(){}
}
