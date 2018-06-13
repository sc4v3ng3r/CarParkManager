package com.devel.boaventura.carparkingmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by scavenger on 4/26/18.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME= "CAR_PARKING_MANAGER_DB";

    public static final String COLUMN_ID = "_ID";

    private static final String FOREIGN_KEY = "FOREIGN KEY";
    private static final String REFERENCES = "REFERENCES ";
    private static final String ADD_COLUMN = "ADD COLUMN";

    public static final String TABLE_APP_SETTINGS = "TABLE_APP_SETTINGS";
    public static final String APP_SETTINGS_PERFIL_ID = "PERFIL_ID";
    public static final String APP_SETTINGS_TAX_ID = "TAX_ID";

    /*TABLE VEICULO*/
    public static final String TABLE_VEHICLE = "VEHICLE";
    public static final String VEHICLE_COLUMN_LICENSE_PLATE = "LICENSE_PLATE";

    /*TABELA PERFIL*/
    public static final String TABLE_PERFIL = "PERFIL";
    public static final String PERFIL_COLUMN_NAME = "NAME";

    /*TABELA CLIENT*/
    public static final String TABLE_CLIENT = "CLIENT";
    public static final String CLIENT_COLUMN_NAME = "NAME";

    /*TABLE PARK == PARK_SERVICE*/
    public static final String TABLE_PARK = "PARK";
    public static final String PARK_COLUMN_TYPE = "TYPE";
    public static final String PARK_COLUMN_TIME = "TIME";
    public static final String PARK_COLUMN_VALUE = "VALUE";
    public static final String PARK_COLUMN_ID_VACANCY = "ID_VACANCY";
    public static final String PARK_COLUMN_ID_VEHICLE = "ID_VEHICLE";

    /*TABLE_VACANCY*/
    public static final String TABLE_VACANCY = "VACANCY";
    public static final String VACANCY_COLUMN_NUMBER = "NUMBER";
    public static final String VACANCY_COLUMN_TYPE = "TYPE";
    public static final String VACANCY_COLUMN_STATE = "STATE";
    public static final String VACANCY_COLUMN_ID_PERFIL = "ID_PERFIL";
    public static final String VACANCY_COLUMN_ID_CLIENT = "ID_CLIENT";

    /*TABELA RENTING*/
    public static final String TABLE_RENTING = "RENTING";
    public static final String RENTING_COLUMN_ID_CLIENT = "ID_CLIENT";
    public static final String RENTING_COLUMN_ID_VACANCY = "ID_VACANCY";
    public static final String RENTING_COLUMN_VALUE = "VALUE";
    public static final String RENTING_COLUMN_START_TIME = "START_TIME";
    public static final String RENTING_COLUMN_END_TIME = "END_TIME";
    public static final String RENTING_COLUMN_STATE = "STATE";
    public static final String RENTING_COLUMN_IS_PAID = "IS_PAID";


    /*TABELA TAX (taxas)*/
    public static final String TABLE_TAX = "TAX";
    public static final String TAX_COLUMN_NAME = "NAME";
    public static final String TAX_COLUMN_UNTIL_1H = "UNTIL_1H";
    public static final String TAX_COLUMN_FROM_1H_TO_4H = "FROM_1H_TO_4H";
    public static final String TAX_COLUMN_FROM_4H_TO_8H = "FROM_4H_TO_8H";
    public static final String TAX_COLUMN_AFTER_8H = "AFTER_8H";

    public DataBaseHelper(Context context) {
        super(context, NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_VEHICLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                VEHICLE_COLUMN_LICENSE_PLATE + " TEXT NOT NULL)";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_CLIENT + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL," +
                CLIENT_COLUMN_NAME + " TEXT NOT NULL)";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_PERFIL + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PERFIL_COLUMN_NAME + " TEXT NOT NULL)";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_PARK + " ("+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PARK_COLUMN_TYPE + " INTEGER," +
                PARK_COLUMN_TIME + " INTEGER," +
                PARK_COLUMN_VALUE + " REAL)";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_VACANCY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                VACANCY_COLUMN_NUMBER + " TEXT," +
                VACANCY_COLUMN_STATE + " INTEGER," +
                VACANCY_COLUMN_TYPE + " INTEGER) "
                /*VACANCY_COLUMN_ID_PERFIL + " INTEGER)" chave estrangeira*/;
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_RENTING + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
               /* RENTING_COLUMN_ID_CLIENT + " INTEGER," chave estrangeira*/
                RENTING_COLUMN_START_TIME + " INTEGER," +
                RENTING_COLUMN_END_TIME + " INTEGER," +
                RENTING_COLUMN_STATE + " INTEGER," +
                RENTING_COLUMN_VALUE + " REAL," +
                /*RENTING_COLUMN_ID_VACANCY + " INTEGER," + chave estrangeira*/
                RENTING_COLUMN_IS_PAID + " INTEGER)";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_APP_SETTINGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                APP_SETTINGS_PERFIL_ID + " INTEGER, " +
                APP_SETTINGS_TAX_ID + " INTEGER )";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_TAX + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TAX_COLUMN_NAME + " TEXT," +
                TAX_COLUMN_UNTIL_1H + " REAL," +
                TAX_COLUMN_FROM_1H_TO_4H + " REAL," +
                TAX_COLUMN_FROM_4H_TO_8H + " REAL," +
                TAX_COLUMN_AFTER_8H + " REAL)";
        db.execSQL(query);

        /**===================
         * ====================================================
         * CRIACAO DAS CHAVES ESTRANGEIRAS                                       |
         **========================================================================*/

        /*FOREIGN KEY DE TABLE PARK*/
        query = "ALTER TABLE " + TABLE_PARK + " " + ADD_COLUMN + " " +
                PARK_COLUMN_ID_VACANCY + " INTEGER " + REFERENCES +
                TABLE_VACANCY + " (" + COLUMN_ID + ")";

        db.execSQL(query);

        query = "ALTER TABLE " + TABLE_PARK + " " + ADD_COLUMN + " " +
                PARK_COLUMN_ID_VEHICLE + " INTEGER " + REFERENCES +
                TABLE_VEHICLE + " (" + COLUMN_ID + ")";
        db.execSQL(query);
        /*==================================================*/

        /*FOREIGN KEY DE TABLE_VACANCY */
        query = "ALTER TABLE " + TABLE_VACANCY + " " + ADD_COLUMN + " " +
                VACANCY_COLUMN_ID_PERFIL + " INTEGER " + REFERENCES +
                TABLE_PERFIL + " (" + COLUMN_ID + ")";
        db.execSQL(query);

        query = "ALTER TABLE " + TABLE_VACANCY + " " + ADD_COLUMN + " " +
                VACANCY_COLUMN_ID_CLIENT + " INTEGER " + REFERENCES +
                TABLE_CLIENT + " (" + COLUMN_ID + ")";
        db.execSQL(query);

        /*FOREIGN KEY DE TABLE_RENTING */
        query = "ALTER TABLE " + TABLE_RENTING + " " + ADD_COLUMN + " " +
                RENTING_COLUMN_ID_VACANCY + " INTEGER " + REFERENCES +
                TABLE_VACANCY + " (" + COLUMN_ID + ")";
        db.execSQL(query);

        query = "ALTER TABLE " + TABLE_RENTING + " " + ADD_COLUMN + " " +
                RENTING_COLUMN_ID_CLIENT + " INTEGER " + REFERENCES +
                TABLE_CLIENT + " (" + COLUMN_ID + ")";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
