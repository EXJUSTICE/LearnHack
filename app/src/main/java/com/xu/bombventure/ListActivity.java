package com.xu.bombventure;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ListActivity extends AppCompatActivity {
    RecyclerView roomView;
    RoomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String json;

    ArrayList<String> roomNames;
    ArrayList<String>roomIDs;
    ArrayList<String>roomCapacities;
    ArrayList<String>roomArea;
    Button viewNearby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        roomNames = new ArrayList<String>();
        roomIDs = new ArrayList<String>();
        roomCapacities= new ArrayList<String>();
        roomArea = new ArrayList<String>();


        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        roomCapacities= parseJSONintoCapacity(json);

        roomNames=parseJSONintoGSON(json);
        roomArea =parseJSONintoArea(json);

        roomIDs=parseJSONintoID(json);

        //JSON works

        viewNearby = (Button)findViewById(R.id.viewNearby);
        viewNearby.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent launchNearby = new Intent(ListActivity.this, MainActivity.class);
                launchNearby.putExtra("json",json);
                startActivity(launchNearby);
            }
        });


        roomView = (RecyclerView)findViewById(R.id.roomRecyclerView);

        /*roomCapacities = new ArrayList<String>();
        roomArea=new ArrayList<String>();
        roomArea=parseJSONintoArea(json);
        roomCapacities=parseJSONintoCapacity(json);

        */


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        roomView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        roomView.setLayoutManager(mLayoutManager);



        if (mAdapter == null) {
            //bind topics to adapter, then to RecyclerView
            //position and such are handled by adapter
            mAdapter = new RoomAdapter(this,roomNames,roomIDs,roomCapacities,roomArea);
            roomView.setAdapter(mAdapter);

        } else {
            mAdapter.notifyDataSetChanged();
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


    public ArrayList<String> parseJSONintoID(String json){
        ArrayList<String>IDlist= new ArrayList<String>();
        try{
            JSONArray response= new JSONArray(json);

            for(int i =0;i<response.length();i++){
                JSONObject object = response.getJSONObject(i);
                String name =object.getString("ROOMID");
                IDlist.add(name.toString());
                /*Toast toast =Toast.makeText(this,name,Toast.LENGTH_SHORT);
                toast.show();
                */

            }

        }catch (org.json.JSONException e){
            e.printStackTrace();
        }

        return IDlist;


    }

    public ArrayList<String> parseJSONintoCapacity(String json){
        ArrayList<String>Capacitieslist= new ArrayList<String>();
        try{
            JSONArray response= new JSONArray(json);

            for(int i =0;i<response.length();i++){
                JSONObject object = response.getJSONObject(i);
                String capacity =object.getString("CAPACITY");
                Capacitieslist.add(capacity.toString());
                /*Toast toast =Toast.makeText(this,capacity,Toast.LENGTH_SHORT);
                toast.show();
                */


            }

        }catch (org.json.JSONException e){
            e.printStackTrace();
        }

        return Capacitieslist;


    }

    public ArrayList<String> parseJSONintoArea(String json){
        ArrayList<String>Areaslist= new ArrayList<String>();
        try{
            JSONArray response= new JSONArray(json);

            for(int i =0;i<response.length();i++){
                JSONObject object = response.getJSONObject(i);
                String area =object.getString("ROOMAREA");
                Areaslist.add(area.toString());
                /*Toast toast =Toast.makeText(this,name,Toast.LENGTH_SHORT);
                toast.show();
                */

            }

        }catch (org.json.JSONException e){
            e.printStackTrace();
        }

        return Areaslist;


    }



    private class RoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String Name;
        private String Descrip;
        TextView description;
        TextView nameView;
        ImageView image;





        public RoomHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            description = (TextView)itemView.findViewById(R.id.descriptionView);
            nameView =(TextView)itemView.findViewById(R.id.namerowView);
            image=(ImageView)itemView.findViewById(R.id.imageView2);



        }


        //Every OnClick to a recyclverView item either adds or removes
        @Override
        public void onClick(View v) {
           Intent launch = new Intent(ListActivity.this, DetailActivity.class);
            launch.putExtra("name",Name);
            startActivity(launch);
        }




    }

    private class RoomAdapter extends RecyclerView.Adapter<RoomHolder> {
        //Similiar to RowAdapter before in ListView, returns a custom view of a format row
        private ArrayList<String>name;
        private ArrayList<String> Descrip;
        private ArrayList<String>Caps;
        private ArrayList<String>Areas;

        private Context mContext;

        //constructor assigning  argument to our own
        public RoomAdapter(Context context, ArrayList<String>names, ArrayList<String>IDs, ArrayList<String>capacities, ArrayList<String>areas) {
            this.name = names;
            this.mContext = context;
            this.Descrip=IDs;
            this.Caps=capacities;
            this.Areas =areas;


        }

        @Override
        // onCreateViewHolder returns each row view as a TopicHolder, DEFINED PREVIOUSLY
        public RoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //use parent activity layoutinflater

            //inflate row
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list, parent, false);
            RoomHolder roomholder = new RoomHolder(view);

            return roomholder;
        }


        @Override
        public void onBindViewHolder(RoomHolder holder, int position){


            holder.Name= name.get(position);
            holder.nameView.setText(name.get(position));
            holder.description.setText( " Room Capacity:"+Caps.get(position)+" Room Area:"+ Areas.get(position));
            //+ " Room Capacity:" + Caps.get(position)+" Room Area:"+Areas.get(position)
        }

        @Override
        public int getItemCount() {
            return name.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView)
        {
            super.onAttachedToRecyclerView(recyclerView);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        return true;
    }





}
