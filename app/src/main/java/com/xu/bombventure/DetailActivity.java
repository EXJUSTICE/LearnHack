package com.xu.bombventure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//DetailActivity, will have a list of descriptions along with picture
//disabled checkboxes maybe
//with a button to launch updateActivity

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}
