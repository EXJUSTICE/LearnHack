package com.xu.bombventure;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    Room[] rooms;
    ArrayList<String> roomNames;
    ArrayList<String>roomIDs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        //turns rooms into an actual class with GSON

        roomView = (RecyclerView)findViewById(R.id.roomRecyclerView);
        roomNames = new ArrayList<String>();
        roomIDs = new ArrayList<String>();


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        roomView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        roomView.setLayoutManager(mLayoutManager);
        roomNames=parseJSONintoGSON(json);
        roomIDs=parseJSONintoID(json);


        if (mAdapter == null) {
            //bind topics to adapter, then to RecyclerView
            //position and such are handled by adapter
            mAdapter = new RoomAdapter(this,roomNames,roomIDs);
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

        private Context mContext;

        //constructor assigning  argument to our own
        public RoomAdapter(Context context, ArrayList<String>names, ArrayList <String>IDs) {
            this.name = names;
            this.mContext = context;
            this.Descrip=IDs;


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
            holder.description.setText(Descrip.get(position));
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



}
