package com.xu.bombventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

//Called when you want to update somepage
//TODO Should have an imageView that loads user gallery click
public class UpdateActivity extends AppCompatActivity {
    Spinner mSpinner;
    Button share;
    String message;
    CheckBox ac;
    CheckBox wheelchair;
    CheckBox windows;
    CheckBox ppt;
    CheckBox whiteboard;
    String roomname;
    TextView banner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        share =(Button)findViewById(R.id.updateplz);
        banner = (TextView)findViewById(R.id.textView3);

        Intent intent = getIntent();
        roomname =intent.getStringExtra("name");
        banner.setText("Please enter the correct criteria for " + roomname);

        ac= (CheckBox)findViewById(R.id.checkBox5);
        wheelchair= (CheckBox)findViewById(R.id.checkBox6);
        windows= (CheckBox)findViewById(R.id.checkBox7);
        ppt= (CheckBox)findViewById(R.id.checkBox8);
        whiteboard= (CheckBox)findViewById(R.id.checkBox4);
        mSpinner =(Spinner)findViewById(R.id.peopleNo);
        String entries[] = {"5","10","20","30","50","100"};

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,entries ); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerArrayAdapter);

        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                StringBuilder sb= new StringBuilder("There are inconsistencies in the detailed information for this room. \n\n");

                sb.append("The real occupancy is " +mSpinner.getSelectedItem().toString()+".\n");

                if(!ac.isChecked()){
                    sb.append("There is no air conditioning in this room.\n\n");
                }if(!wheelchair.isChecked()){
                    sb.append("The room is not wheelchair accessible. \n\n");
                }if(!windows.isChecked()){
                    sb.append("The windows are poor or broken. \n\n");
                }if(!ppt.isChecked()){
                    sb.append("The room is not presentation ready. \n\n");
                }if(!whiteboard.isChecked()) {
                    sb.append("There is no whiteboard in this room. \n\n");
                }

                message =sb.toString();



                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(
                            android.content.Intent.EXTRA_SUBJECT, "Errors in room description for  "+roomname);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, message);

                    startActivity(sharingIntent);


            }
        });
    }
}
