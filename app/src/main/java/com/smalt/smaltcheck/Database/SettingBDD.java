package com.smalt.smaltcheck.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smalt.smaltcheck.Entity.Alert;
import com.smalt.smaltcheck.Entity.Setting;

import java.util.HashMap;

/**
 * Created by yassinebouderbala on 11/10/2015.
 */
public class SettingBDD {
    private static final int VERSION_BDD = Version.num;
    private static final String NOM_BDD = "check.db";

    private static final String TABLE_SETTING = "setting";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_ALERT = "ALERT";
    private static final int NUM_COL_ALERT = 1;
    private static final String COL_TIMEREFRESH = "TIMEREFRESH";
    private static final int NUM_COL_TIMEREFRESH = 2;

    private SQLiteDatabase bdd;

    private MyBaseSQLite myBaseSQLite;

    public SettingBDD(Context context){
        myBaseSQLite = new MyBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        bdd = myBaseSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public void insertSetting(Setting setting){
        try {
            ContentValues values = new ContentValues();
            values.put(COL_ALERT, setting.getAlert());
            values.put(COL_TIMEREFRESH, setting.getTimerefresh());
            bdd.insert(TABLE_SETTING, null, values);
        }catch (Exception e){}
    }

    public Setting updateSetting(Setting setting){
        ContentValues data=new ContentValues();
        data.put(COL_ALERT,setting.getAlert());
        data.put(COL_TIMEREFRESH,setting.getTimerefresh());
        bdd.update(TABLE_SETTING, data, "id=" + setting.getId(), null);
        return getSetting();
    }

    public Setting getSetting(){
        Setting setting = new Setting();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SETTING;

        try {

            Cursor cursor = bdd.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        setting.setId(Long.parseLong(cursor.getString(0)));
                        setting.setAlert(cursor.getString(1));
                        setting.setTimerefresh(Integer.parseInt(cursor.getString(2)));
                    } while (cursor.moveToNext());
                }

            } finally {}

        } finally {}

        return setting;
    }

}
