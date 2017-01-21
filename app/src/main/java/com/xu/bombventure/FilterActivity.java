package com.xu.bombventure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

/**
 * First Main page, basically allows user to input data
 * TODO add in recyclerView for checkboxes instead
 */

public class FilterActivity extends AppCompatActivity {
    Spinner peopleNo;
    CheckBox wheelchair;
    CheckBox aircon;
    CheckBox windows;
    CheckBox ppt;
    CheckBox whiteboard;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    int maxNo;

    Button launchScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        peopleNo = (Spinner)findViewById(R.id.peopleNo);
        wheelchair = (CheckBox)findViewById(R.id.checkBox6);
        aircon = (CheckBox)findViewById(R.id.checkBox5);
        windows = (CheckBox)findViewById(R.id.checkBox7);
        ppt = (CheckBox)findViewById(R.id.checkBox6);
        whiteboard= (CheckBox)findViewById(R.id.checkBox4);
        launchScan = (Button)findViewById(R.id.launch);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor= settings.edit();

        launchScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wheelchair.isChecked()){
                    editor.putBoolean("wheelchair",true);
                    editor.commit();
                }
                if (aircon.isChecked()){
                    editor.putBoolean("aircon",true);
                    editor.commit();
                }
                if (windows.isChecked()){
                    editor.putBoolean("windows",true);
                    editor.commit();
                }
                if (ppt.isChecked()){
                    editor.putBoolean("pptready",true);
                    editor.commit();
                }
                if (whiteboard.isChecked()){
                    editor.putBoolean("whitboard",true);
                    editor.commit();
                }

                //Finally add in the spinner
                maxNo =Integer.parseInt(peopleNo.getSelectedItem().toString());
                editor.putInt("maxNo",maxNo);

                editor.commit();

                //TODO networking code here?  launch and return an arrayList of Rooms

                Intent launchMap = new Intent(FilterActivity.this,MainActivity.class);
                startActivity(launchMap);
            }
        });

    }

}
