package com.xu.bombventure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Serves as an intermediate to launch AnswerActivity with StartActivityForResult
 * bouncing the latitude onward to defuseactivity as an identifier
 * TODO figure out a use for this, otherwise delete straight
 */
public class PlaceholderActivity extends AppCompatActivity{
    int ANSWER_QUESTION= 1;
    int ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ID= intent.getIntExtra("ID", 0);
        Intent launchAnswer= new Intent(this,AnswerActivity.class);
        launchAnswer.putExtra("ID",ID);
        startActivityForResult(launchAnswer, ANSWER_QUESTION);
        //dismiss();?
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //DialogFragment reporting back that we can move one to next Topic
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        //If question answered successfully, we return to mainactivity
        if(requestCode==ANSWER_QUESTION){
            Intent returnMap = new Intent(this,MainActivity.class);
            //In any case, we will still return back to  MapActivity

            startActivity(returnMap);
            finish();
        }
    }

}
