package com.app.bandeco;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import erick.bandeco.database.DatabaseContract;
import erick.bandeco.model.Day;
import erick.bandeco.model.Meal;
import erick.bandeco.view.Settings;

import static com.app.bandeco.Constants.DAYS_TO_NOTIFY_CODES;

public final class Utils {

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static void changeStatusColor(ActionBarActivity activity, View parentLayout) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
					Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {  // Let Lollipop handle this
			parentLayout.setFitsSystemWindows(true);
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			SystemBarTintManager tintManager = new SystemBarTintManager(activity);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.status_color));
		}
	}

	public static boolean[] getBooleansFromDaysCode(int daysCode) {
		boolean[] result = new boolean[7];

		for (int i = 0; i < DAYS_TO_NOTIFY_CODES.length; i++)
			result[i] = (daysCode & DAYS_TO_NOTIFY_CODES[i]) != 0;

		return result;
	}

	public static boolean shouldDisplayMeal(int mealType, int mealOption){
		switch (mealType){
			case Constants.MEAL_TYPE_LUNCH :
				return mealOption != Constants.MEAL_OPTION_DINNER_ONLY;
			case Constants.MEAL_TYPE_DINNER :
				return mealOption != Constants.MEAL_OPTION_LUNCH_ONLY;
			default:
				return false;
		}
	}

	public static boolean shouldNotifyToday(int daysToNotifyCode, Calendar today) {
		int todayNumber = Day.adaptDayOfWeek(today.get(Calendar.DAY_OF_WEEK));

		return (daysToNotifyCode & DAYS_TO_NOTIFY_CODES[todayNumber]) != 0;
	}

	public static <T> void arraySwap(T[] array, int pos1, int pos2) {
		T tmp = array[pos1];
		array[pos1] = array[pos2];
		array[pos2] = tmp;
	}

	public static Integer[] extractMenuCodesFromInt(int coded) {
		Integer[] result = new Integer[7];

		for (int i = 0; i < result.length; i++) {
			result[i] = coded & 0x07;
			coded = coded >> 3;
		}

		return result;
	}

	public static int codifyMenuCodesFromArray(Integer[] array) {
		int result = 0;

		for (int i = 0; i < array.length; i++)
			result |= array[i] << (3 * i);

		return result;
	}

	public static String[] sortMenuEntries(String[] stringArray, Integer[] menuEntriesOrder) {
		String[] result = new String[stringArray.length];

		for (Integer i : menuEntriesOrder)
			result[i] = stringArray[menuEntriesOrder[i]];

		return result;
	}

	public static Boolean[] extractEnabledMenuEntriesFromInt(int coded, Integer[] menuEntriesOrder) {
		Boolean[] result = new Boolean[menuEntriesOrder.length];

		for (int i = 0; i < menuEntriesOrder.length; i++) {
			int pos = menuEntriesOrder[i];
			result[i] = (coded & (1 << pos)) != 0;
		}

		return result;
	}

	public static int codifyEnabledMenuEntriesFromArray(Boolean[] enabledMenuEntries, Integer[] menuEntriesOrder) {
		int code = 0;

		for (int i = 0; i < menuEntriesOrder.length; i++) {
			int pos = menuEntriesOrder[i];
			if (enabledMenuEntries[i])
				code |= 1 << pos;
		}

		return code;
	}

	public static String getMealType(Meal meal, Context context) {
		if (meal.getType() == Constants.MEAL_TYPE_LUNCH)
			return context.getResources().getString(R.string.lunch);
		else
			return context.getResources().getString(R.string.dinner);
	}

	public static String getTextFromMeal(Meal meal, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), 0);

		int menuEntriesOrderCoded = preferences.getInt(Settings.MENU_ENTRIES_ORDER, Constants.DEFAULT_MENU_ENTRIES_CODE);
		Integer[] menuEntriesOrder = Utils.extractMenuCodesFromInt(menuEntriesOrderCoded);

		int enabledMenuEntriesCoded = preferences.getInt(Settings.ENABLED_MENU_ENTRIES, Constants.ALL_MENU_ENTRIES_CODE);
		Boolean[] enabledMenuEntries = Utils.extractEnabledMenuEntriesFromInt(enabledMenuEntriesCoded, menuEntriesOrder);

		String text = "";
		String[] menuEntries = context.getResources().getStringArray(R.array.menu_entries);

		for (int i = 0; i < menuEntriesOrder.length; i++) {
			if (!enabledMenuEntries[i])
				continue;

			text += menuEntries[menuEntriesOrder[i]] + ": ";
			switch (menuEntriesOrder[i]) {
				case Constants.CODE_OF_PRATO_PRINCIPAL:
					text += meal.getPratoPrincipal() + "\n";
					break;
				case Constants.CODE_OF_PRATO_VEGETARIANO:
					text += meal.getPratoVegetariano() + "\n";
					break;
				case Constants.CODE_OF_ACOMPANHAMENTO:
					text += meal.getAcompanhamento() + "\n";
					break;
				case Constants.CODE_OF_GUARNICAO:
					text += meal.getGuarnicao() + "\n";
					break;
				case Constants.CODE_OF_ENTRADA:
					text += meal.getEntrada() + "\n";
					break;
				case Constants.CODE_OF_SOBREMESA:
					text += meal.getSobremesa() + "\n";
					break;
				case Constants.CODE_OF_REFRESCO:
					text += meal.getRefresco() + "\n";
					break;
			}
		}

		return text;
	}

	//TODO use a calendar instead of day
	public static String getDayOfTheWeek(Day day, Context context) {
		String[] daysOfTheWeek = context.getResources().getStringArray(R.array.days_array);
		int weekDay = day.getWeekDay() % 7;

		return daysOfTheWeek[weekDay];
	}

	public static String readAssetAndClose(AssetManager assetManager, String assetName) {
		String assetData = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(assetName)));
			String line;
			while ((line = bufferedReader.readLine()) != null)
				assetData += line;

			bufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return assetData;
	}

	public static Intent getInvitationIntent(Context context, String mealType, String mealBody) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		String msg = String.format(context.getString(R.string.inviting_you), mealType.toLowerCase()) + "\n" + mealBody;
		shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
		return shareIntent;
	}

	public static boolean hasDislikedItem(Meal meal, SQLiteDatabase db, Context context){
		ArrayList<String> dislikeList = OperationsWithDB.getListFromDB(db, DatabaseContract.NegativeWords.TABLE_NAME, new String[]{DatabaseContract.NegativeWords.WORDS});

		return hasMatch(meal, dislikeList, context);
	}

	public static boolean hasLikedItem(Meal meal, SQLiteDatabase db, Context context){
		ArrayList<String> likeList = OperationsWithDB.getListFromDB(db, DatabaseContract.PositiveWords.TABLE_NAME, new String[]{DatabaseContract.PositiveWords.WORDS});

		return hasMatch(meal, likeList, context);
	}

	public static boolean hasMatch(Meal meal, ArrayList<String> list, Context context) {
		String tmp = getTextFromMeal(meal, context).toLowerCase();

		for (String s : list)
			if (tmp.contains(s.toLowerCase()))
				return true;

		return false;
	}

	private Utils(){}
}
