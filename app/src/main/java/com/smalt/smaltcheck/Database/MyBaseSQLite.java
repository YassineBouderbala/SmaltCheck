package com.smalt.smaltcheck.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smalt.smaltcheck.Activity.MainActivity;

/**
 * Created by yassinebouderbala on 10/10/2015.
 */
public class MyBaseSQLite extends SQLiteOpenHelper{
    private static final String TABLE_ALERT = "alert";
    private static final String COL_ID = "ID";
    private static final String COL_URL = "URL";
    private static final String COL_ERRORCODE = "ERRORCODE";

    private static final String TABLE_SETTING = "setting";
    private static final String COL_ALERT = "ALERT";
    private static final String COL_TIMEREFRESH = "TIMEREFRESH";

    private static final String CREATE_BDD_ALERT = "CREATE TABLE IF NOT EXISTS " + TABLE_ALERT + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_URL + " TEXT NOT NULL UNIQUE, "
            + COL_ERRORCODE + " INTEGER NOT NULL);";

    private static final String CREATE_BDD_SETTING = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTING + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ALERT + " STRING NOT NULL , "
            + COL_TIMEREFRESH + " INTEGER NOT NULL);";

    public MyBaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD_ALERT);
        db.execSQL(CREATE_BDD_SETTING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERT + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING + ";");
        onCreate(db);
    }
}
