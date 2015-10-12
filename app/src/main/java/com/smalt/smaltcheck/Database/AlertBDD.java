package com.smalt.smaltcheck.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smalt.smaltcheck.Entity.Alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yassinebouderbala on 10/10/2015.
 */
public class AlertBDD {

    private static final int VERSION_BDD = Version.num;
    private static final String NOM_BDD = "check.db";

    private static final String TABLE_ALERT = "alert";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_URL = "URL";
    private static final int NUM_COL_URL = 1;
    private static final String COL_ERRORCODE = "ERRORCODE";
    private static final int NUM_COL_ERRORCODE = 2;

    private SQLiteDatabase bdd;

    private MyBaseSQLite myBaseSQLite;

    public AlertBDD(Context context){
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

    public void insertAlert(Alert alert){
        try {
            ContentValues values = new ContentValues();
            values.put(COL_URL, alert.getUrl());
            values.put(COL_ERRORCODE, alert.getErrorCode());
            bdd.insert(TABLE_ALERT, null, values);
        }catch (Exception e){}
    }

    public HashMap<String,Integer> getAllAlerts(){
        HashMap<String,Integer> alerts = new HashMap<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ALERT;

        try {

            Cursor cursor = bdd.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Alert alert = new Alert();
                        //only one column
                        alert.setId(Long.parseLong(cursor.getString(0)));
                        alert.setUrl(cursor.getString(1));
                        alert.setErrorCode(Integer.parseInt(cursor.getString(2)));
                        alerts.put(alert.getUrl(),alert.getErrorCode());
                    } while (cursor.moveToNext());
                }

            } finally {}

        } finally {}

        return alerts;
    }

    public Alert getAlertWithUrl(String url){
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_ALERT, new String[] {COL_ID, COL_URL, COL_ERRORCODE}, COL_URL + " LIKE \"" + url +"\"", null, null, null, null);
        return cursorToAlert(c);
    }

    private Alert cursorToAlert(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();

        Alert alert = new Alert();

        alert.setId(c.getLong(NUM_COL_ID));
        alert.setUrl(c.getString(NUM_COL_URL));
        alert.setErrorCode(c.getInt(NUM_COL_ERRORCODE));

        c.close();

        return alert;
    }

    public boolean deleteById(Long id)
    {
        return bdd.delete(TABLE_ALERT, COL_ID + "=" + id, null) > 0;
    }
}
