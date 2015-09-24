package erick.bandeco.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static erick.bandeco.database.DatabaseContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "Bandeco.db";
	private static final int DATABASE_VERSION = 3;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Meals.CREATE_TABLE);
		db.execSQL(PositiveWords.CREATE_TABLE);
		db.execSQL(NegativeWords.CREATE_TABLE);
		db.execSQL(LastUpdate.CREATE_TABLE);
		db.execSQL(MealsDate.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Meals.DESTROY_TABLE);
		db.execSQL(PositiveWords.DESTROY_TABLE);
		db.execSQL(NegativeWords.DESTROY_TABLE);
		db.execSQL(LastUpdate.DESTROY_TABLE);
		db.execSQL(MealsDate.DESTROY_TABLE);

		onCreate(db);
	}
}
