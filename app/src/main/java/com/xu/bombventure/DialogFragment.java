package com.xu.bombventure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Omistaja on 15/11/2016.
 */
public class DialogFragment extends android.support.v4.app.DialogFragment  {

    public DialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);

    }

    @Override
    //onCreateDialog vs OnCreateView?
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //need to design dialo
        setStyle(STYLE_NO_TITLE, 0);
        View dialogView = inflater.inflate(R.layout.dialogfrag, container, false);

        Button returnButton= (Button)dialogView.findViewById(R.id.toMapBtn);



        returnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //return to AnswerActivity temporarily
                AnswerActivity.answeredcorrectly=true;
                dismiss();
            }
        });
        return dialogView;
    }







}



