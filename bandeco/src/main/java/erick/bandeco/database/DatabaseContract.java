package erick.bandeco.database;

public abstract class DatabaseContract {

    public static abstract class LastUpdate{
        public static final String TABLE_NAME = "last_update";
        public static final String TABLE_ID = "id";
        public static final String DATE = "last_date";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                TABLE_ID + " INTEGER PRIMARY KEY, " +
                DATE + " DATETIME" +
                ");"
                ;

        public static final String DESTROY_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";"
                ;
    }

    public static abstract class Meals {
        public static final String TABLE_NAME = "meals";
        public static final String MEAL_TYPE = "meal_type";
        public static final String DAY = "day";
        public static final String ENTRADA = "entrada";
        public static final String GUARNICAO = "guarnicao";
        public static final String PRATO_PRINCIPAL = "prato_principal";
        public static final String PRATO_VEGETARIANO = "prato_vegeratiano";
        public static final String ACOMPANHAMENTO = "acompanhamento";
        public static final String SOBREMESA = "sobremesa";
        public static final String REFRESCO = "refresco";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ( " +
                MEAL_TYPE + " INTEGER, " +
                DAY + " INTEGER, " +
                ENTRADA + " TEXT, " +
                GUARNICAO + " TEXT, " +
                PRATO_PRINCIPAL + " TEXT, " +
                PRATO_VEGETARIANO + " TEXT, " +
                ACOMPANHAMENTO + " TEXT, " +
                SOBREMESA + " TEXT, " +
                REFRESCO + " TEXT, " +
                "PRIMARY KEY (" + MEAL_TYPE + ", " + DAY + ")" +
                ");"
                ;

        public static final String DESTROY_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";"
                ;
    }

    public static abstract class PositiveWords {
        public static final String TABLE_NAME = "positive_words";
        public static final String ID = "word_id";
        public static final String WORDS = "word";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WORDS + " TEXT" +
                ");"
                ;

        public static final String DESTROY_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";"
                ;
    }

    public static abstract class NegativeWords {
        public static final String TABLE_NAME = "negative_words";
        public static final String WORD_ID = "word_id";
        public static final String WORDS = "word";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ( " +
                WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WORDS + " TEXT" +
                ");"
                ;

        public static final String DESTROY_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";"
                ;
    }
}
