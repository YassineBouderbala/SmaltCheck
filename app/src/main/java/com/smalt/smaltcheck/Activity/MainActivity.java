package com.smalt.smaltcheck.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smalt.smaltcheck.Database.AlertBDD;
import com.smalt.smaltcheck.Database.SettingBDD;
import com.smalt.smaltcheck.Entity.Alert;
import com.smalt.smaltcheck.Entity.Setting;
import com.smalt.smaltcheck.R;
import com.smalt.smaltcheck.Request.Get;
import com.smalt.smaltcheck.Utils.*;
import com.smalt.smaltcheck.Utils.Thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);

        /* Initialisation alerts */

        AlertBDD alerts = new AlertBDD(getApplicationContext());
        alerts.open();

        if(alerts.getAllAlerts().size() != 0 ){
            new refresh_url().execute(); // on affiche toutes les urls dispo
        }

        alerts.close();
        /* Initialisation settings */

        final SettingBDD settingBdd = new SettingBDD(this);
        settingBdd.open();

        if(settingBdd.getSetting().getAlert() == null){ // SI il n'ya aucun setting alors on initialise
            Setting setting = new Setting("true",300000);
            settingBdd.insertSetting(setting);
        }

        settingBdd.close();

        /* THREAD UTIL */

        SettingBDD settingData = new SettingBDD(getApplicationContext());
        settingData.open();

        Thread.secondTread = false;

        handler.postDelayed(new Runnable() {
            public void run() {
                SettingBDD settingData = new SettingBDD(getApplicationContext());
                settingData.open();

                AlertBDD alerts = new AlertBDD(getApplicationContext());
                alerts.open();

                if (settingData.getSetting().getAlert().equals("true")) { // Si il y'a une alerte alors on alerte
                    Link.urls = alerts.getAllAlerts();

                    for (Map.Entry<String, Integer> entry : Link.urls.entrySet()) {
                        new getErrorCodes().execute(String.valueOf(entry.getKey()));
                    }

                    handler.postDelayed(this, settingData.getSetting().getTimerefresh());

                    for (Map.Entry<String, Integer> entry : Link.urls.entrySet()) {
                        if (entry.getValue() > 309) {
                            createNotification(entry.getKey(), entry.getValue());
                        }
                    }
                }
                settingData.close();
                alerts.close();
            }
        }, settingData.getSetting().getTimerefresh());

    }

    /************** ONPAUSE **************/

    @Override
    public void onPause(){
        super.onPause();

        SettingBDD settingData = new SettingBDD(getApplicationContext());
        settingData.open();

        handler.postDelayed(new Runnable() {
            public void run() {
                SettingBDD settingData = new SettingBDD(getApplicationContext());
                settingData.open();

                AlertBDD alerts = new AlertBDD(getApplicationContext());
                alerts.open();

                if (settingData.getSetting().getAlert().equals("true")) { // Si il y'a une alerte alors on alerte
                    Link.urls = alerts.getAllAlerts();

                    for (Map.Entry<String, Integer> entry : Link.urls.entrySet()) {
                        new getErrorCodes().execute(String.valueOf(entry.getKey()));
                    }

                    handler.postDelayed(this, settingData.getSetting().getTimerefresh());

                    for (Map.Entry<String, Integer> entry : Link.urls.entrySet()) {
                        if (entry.getValue() > 309) {
                            createNotification(entry.getKey(), entry.getValue());
                        }
                    }
                }
                settingData.close();
                alerts.close();
            }
        }, settingData.getSetting().getTimerefresh());
    }

    /************** Notifications **************/

    private final void createNotification(String url, Integer errorCode){
        Link.cmpAlert++;

        //Récupération du notification Manager
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //Création de la notification avec spécification de l'icône de la notification et le texte qui apparait à la création de la notification
        final Notification notification = new Notification(R.drawable.warning, Message.title(url), System.currentTimeMillis());

        //Définition de la redirection au moment du clic sur la notification. Dans notre cas la notification redirige vers notre application
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        //Récupération du titre et description de la notification
        final String notificationTitle = Message.title(url);
        final String notificationDesc = Message.description(url,errorCode);

        //Notification & Vibration
        notification.setLatestEventInfo(this, notificationTitle, notificationDesc, pendingIntent);

        notificationManager.notify(Link.cmpAlert, notification);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(new long[]{0, 500}, -1);

        Link.cmpAlert++;
        if(Link.cmpAlert == 1000){
            Link.cmpAlert = 0;
        }
    }

    /************** GET error codes **************/

    private class getErrorCodes extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... url) {
            Link.urls.put(url[0], Get.send(url[0]));
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }

    /************** Initialisation **************/

    private class initialisation extends AsyncTask<String, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=ProgressDialog.show(MainActivity.this,"","Loading...",false);
        }

        @Override
        protected Void doInBackground(String... url) {
            AlertBDD alertBdd = new AlertBDD(getApplicationContext());

            alertBdd.open();
            alertBdd.insertAlert(new Alert(url[0], Get.send(url[0])));
            alertBdd.close();

            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
        }
    }

    /************** Refresh **************/

    private class refresh_url extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=ProgressDialog.show(MainActivity.this,"","Loading urls...",false);
        }

        @Override
        protected Void doInBackground(Void... url) {

            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            final AlertBDD alerts = new AlertBDD(getApplicationContext());
            alerts.open();

            LinearLayout parent_layout = (LinearLayout) findViewById(R.id.parent_layout_list_url);
            parent_layout.removeAllViews();
            for(Map.Entry<String, Integer> entry : alerts.getAllAlerts().entrySet()) {
                RelativeLayout child_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.urls_layout, parent_layout, Boolean.parseBoolean(null));

                TextView tv = (TextView)child_layout.getChildAt(0);
                tv.setText(entry.getKey());

                ImageButton imgButton = (ImageButton)child_layout.getChildAt(1);
                imgButton.setTag(alerts.getAlertWithUrl(String.valueOf(entry.getKey())).getId());

                imgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertBDD alertBdd = new AlertBDD(getApplicationContext());
                        alertBdd.open();
                        alertBdd.deleteById(Long.parseLong(String.valueOf(v.getTag())));
                        alertBdd.close();
                        new refresh_url().execute();
                    }
                });

                parent_layout.addView(child_layout);
            }
            alerts.close();
        }

        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
        }
    }

    /************** MENU **************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.plus:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle(Message.add_url);
                alert.setMessage(Message.format);

                final EditText input = new EditText(this);
                alert.setView(input);

                alert.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.setNegativeButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        new initialisation().execute(String.valueOf(input.getText()));
                        new refresh_url().execute();
                    }
                });

                alert.show();
                break;
            case R.id.setting:
                i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
