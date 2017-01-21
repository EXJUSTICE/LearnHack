package com.xu.bombventure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * First Main page, basically allows user to input data
 * TODO add in recyclerView for checkboxes instead
 */

public class FilterActivity extends AppCompatActivity {
    Spinner peopleNo;

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    int maxNo;
    String JSON;
    RecyclerView filterView;
    FilterAdapter mAdapter;
    ArrayList<String> checkedFilters;
    private RecyclerView.LayoutManager mLayoutManager;

    Button launchScan;
    ArrayList<String> filters;
    ArrayList<String>names;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        peopleNo = (Spinner)findViewById(R.id.peopleNo);
        String entries[] = {"5","10","20","30","50","100"};



        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,  R.layout.spinner_item,entries ); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        peopleNo.setAdapter(spinnerArrayAdapter);



        //adding in the filters programmatically
        filters = new ArrayList<String>();
        filters.add("Air conditioning");
        filters.add("Projector");
        filters.add("HDMI/VGA") ;
        filters.add("Plugs");
        filters.add("Lighting");
        filters.add("CD/DVD input");
        filters.add("Lecture theatre");
        filters.add("Seminar rooms");
        filters.add("Adjustable podium");
        filters.add("Hearing aids");
        filters.add("Wheelchair accessibility");
        filters.add("Lifts");
        filters.add("Catering-ready");
        checkedFilters= new ArrayList<String>();


        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor= settings.edit();
        JSON ="";

        filterView = (RecyclerView)findViewById(R.id.filterRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        filterView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        filterView.setLayoutManager(mLayoutManager);

        if (mAdapter == null) {
            //bind topics to adapter, then to RecyclerView
            //position and such are handled by adapter
            mAdapter = new FilterAdapter(this,filters);
            filterView.setAdapter(mAdapter);

        } else {
            mAdapter.notifyDataSetChanged();
        }


    launchScan = (Button)findViewById(R.id.launchScan);
    launchScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appendURL();
                //Finally add in the spinner
                maxNo =Integer.parseInt(peopleNo.getSelectedItem().toString());
                editor.putInt("maxNo",maxNo);

                editor.commit();

                //iInitialize JSONGet class to parse out a JSON string, saved higher up as JSON
                JSONGet js = new JSONGet();

                js.execute("https://learnhack-roombookings.herokuapp.com/list_rooms");

            }
        });

    }

        public String appendURL() {
            String urlend = "";
            StringBuilder stringBuilder = new StringBuilder();



            for(int i =0;i<checkedFilters.size();i++){
            if (checkedFilters.get(i).equals("Air conditioning")){
                stringBuilder.append("AC-");

            }else if(checkedFilters.get(i).equals("Projector")){
                stringBuilder.append("project-");

            }else if(checkedFilters.get(i).equals("HDMI/VGA")) {
                stringBuilder.append("cables-");

            }else if(checkedFilters.get(i).equals("Plugs")) {
                stringBuilder.append("plugs-");

            }else if(checkedFilters.get(i).equals("Lighting")) {
                stringBuilder.append("light-");

            }else if(checkedFilters.get(i).equals("CD/DVD input")) {
                stringBuilder.append("cd-");
            }else if(checkedFilters.get(i).equals("Lecutre theatre")) {
                stringBuilder.append("LT-");
            }else if(checkedFilters.get(i).equals("Seminar room")) {
                stringBuilder.append("SM-");
            }else if(checkedFilters.get(i).equals("Adjustable podium")) {
                stringBuilder.append("pod-");
            }else if(checkedFilters.get(i).equals("Hearing aids")) {
                stringBuilder.append("deaf-");
            }else if(checkedFilters.get(i).equals("Wheelchair accessibility")) {
                stringBuilder.append("wheelchair-");
            }else if(checkedFilters.get(i).equals("Lifts")) {
                stringBuilder.append("lift-");
            }else if(checkedFilters.get(i).equals("Catering-ready")) {
                stringBuilder.append("food-");
            }


            }
            stringBuilder.append("end");
            urlend=stringBuilder.toString();
            return urlend;
    }

    //ViewHolder class for recycler
    private class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String Filter;

        private CheckBox filterBox;

        public FilterHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            filterBox = (CheckBox)itemView.findViewById(R.id.filterBox);
        }


    //Every OnClick to a recyclverView item either adds or removes
        @Override
        public void onClick(View v) {
            if (filterBox.isChecked()) {
                filterBox.setChecked(false);
                checkedFilters.remove(filterBox.getText().toString());

            } else {
                filterBox.setChecked(true);
                checkedFilters.add(filterBox.getText().toString());


            }

        }


        public void bindFilter(String filter) {
            Filter = filter;
            filterBox.setText(Filter.toString());

        }
    }

        private class FilterAdapter extends RecyclerView.Adapter<FilterHolder> {
            //Similiar to RowAdapter before in ListView, returns a custom view of a format row
            private ArrayList<String> Filters;
            private Context mContext;

            //constructor assigning  argument to our own  TopicAdapter mDialogueTopics list
            public FilterAdapter(Context context,ArrayList<String> filters) {
                this.Filters = filters;
                this.mContext=context;
            }

            @Override
            // onCreateViewHolder returns each row view as a TopicHolder, DEFINED PREVIOUSLY
            public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //use parent activity layoutinflater

                //inflate row
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

                FilterHolder holder = new FilterHolder(view);
                return holder;
            }
            @Override
            public void onBindViewHolder(FilterHolder holder, int position){
                String whichfilter = Filters.get(position);
                holder.filterBox.setText(whichfilter);
            }

            @Override
            public int getItemCount() {
                return Filters.size();
            }

            @Override
            public void onAttachedToRecyclerView(RecyclerView recyclerView) {
                super.onAttachedToRecyclerView(recyclerView);
            }



        }

    public ArrayList<String> parseJSONintoGSON(String json){
        ArrayList<String>nameList = new ArrayList<String>();
        try{
            JSONArray response= new JSONArray(json);

            for(int i =0;i<response.length();i++){
                JSONObject object = response.getJSONObject(i);
                String name =object.getString("NAME");
                nameList.add(name.toString());
                /*Toast toast =Toast.makeText(this,name,Toast.LENGTH_SHORT);
                toast.show();
                */

            }

        }catch (org.json.JSONException e){
            e.printStackTrace();
        }

        return nameList;


    }



            //Inner class AsyncTask for parsing out JSON



public class JSONGet extends AsyncTask<String, String, String> {

    protected void onPreExecute() {
        super.onPreExecute();


    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        JSON = result;
        names= parseJSONintoGSON(JSON);

        Intent launchList = new Intent(FilterActivity.this,ListActivity.class);
        launchList.putExtra("json",JSON);
        startActivity(launchList);

    }
}
}
