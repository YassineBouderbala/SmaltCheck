package com.smalt.smaltcheck.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.smalt.smaltcheck.Database.SettingBDD;
import com.smalt.smaltcheck.Entity.Setting;
import com.smalt.smaltcheck.R;
import com.smalt.smaltcheck.Utils.Message;

import java.util.Set;

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);

        /* Initialisation EDIT REFRESH */

        SettingBDD settingBDD = new SettingBDD(this);

        settingBDD.open();

        Setting setting = settingBDD.getSetting();

        EditText refresh_edit = (EditText)findViewById(R.id.refresh_edit);
        refresh_edit.setText(String.valueOf(setting.getTimerefresh()));

        /* Initialisation SWITCH */

        Switch sw = (Switch) findViewById(R.id.switchAlert);

        if(setting.getAlert().equals("true")){
            sw.setChecked(true);
        }else{
            sw.setChecked(false);
        }

        settingBDD.close();

        /* LISTENER ON SWITCH */

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Switch sw = (Switch) findViewById(R.id.switchAlert);
                SettingBDD settingBDD = new SettingBDD(getApplicationContext());
                settingBDD.open();

                Setting setting = settingBDD.getSetting();

                if (isChecked) {
                    setting.setAlert("true");
                    settingBDD.updateSetting(setting);
                    Intent i = new Intent(SettingActivity.this,MainActivity.class);
                    startActivity(i);
                } else {
                    setting.setAlert("false");
                    settingBDD.updateSetting(setting);
                }

                settingBDD.close();
            }
        });

         /* LISTENER ON BUTTON */

        Button b = (Button)findViewById(R.id.button_save);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText refresh = (EditText) findViewById(R.id.refresh_edit);
                SettingBDD settingBDD = new SettingBDD(getApplicationContext());
                settingBDD.open();

                Setting setting = settingBDD.getSetting();
                setting.setTimerefresh(Integer.parseInt(String.valueOf(refresh.getText())));

                settingBDD.updateSetting(setting);
                Toast.makeText(getApplicationContext(),Message.time_saved, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.home:
                i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.setting:
                i = new Intent(this, SettingActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
