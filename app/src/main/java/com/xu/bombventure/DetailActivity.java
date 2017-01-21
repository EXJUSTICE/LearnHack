package com.xu.bombventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

//DetailActivity, will have a list of descriptions along with picture
//disabled checkboxes maybe
//with a button to launch updateActivity

public class DetailActivity extends AppCompatActivity {
    TextView roomName;
    Button book;
    Button update;
    Button map;
    Spinner peopleNo;
    String roomnom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent =getIntent();
         roomnom =intent.getStringExtra("name");
        roomName= (TextView)findViewById(R.id.roomNameTextView);
        book =(Button) findViewById(R.id.buttonbook);
        update = (Button)findViewById(R.id.buttonupdate);
        map = (Button)findViewById(R.id.map);
        roomName.setText(roomnom);

        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent launchupdate = new Intent(DetailActivity.this, UpdateActivity.class);
                launchupdate.putExtra("name",roomnom);
                startActivity(launchupdate);
            }
        });
        map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent launchMap = new Intent(DetailActivity.this,MainActivity.class);
                startActivity(launchMap);
            }
        });


    }
}
