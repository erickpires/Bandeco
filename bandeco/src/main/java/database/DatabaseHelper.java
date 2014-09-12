package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static database.DatabaseContract.*;

/**
 * Created by erick on 9/10/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Bandeco.db";
    public static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Meals.CREATE_TABLE);
        db.execSQL(PositiveWords.CREATE_TABLE);
        db.execSQL(NegativeWords.CREATE_TABLE);
        db.execSQL(LastUpdate.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Meals.DESTROY_TABLE);
        db.execSQL(PositiveWords.DESTROY_TABLE);
        db.execSQL(NegativeWords.DESTROY_TABLE);
        db.execSQL(LastUpdate.DESTROY_TABLE);
    }
}
