package com.xu.bombventure;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * singleton class, holds rooms
 */

public class RoomDB {

    private static RoomDB sDB;

    private static HashMap<Integer,Room> Database;

    public static RoomDB get(Context context){

        if (sDB == null){
            sDB= new RoomDB(context);
        }
        return sDB;
    }

    private RoomDB(Context context){
        Database = new HashMap<Integer,Room>();

        ArrayList<String>Gavin = new ArrayList<String>();
        Gavin.add("ppt");
        Gavin.add("aircon");

        Database.put(0, new Room("Anatomy G04 Gavin de Beer LT",51.523608,-0.133601,Gavin,100));
    }


    public HashMap<Integer, Room> getRooms() {
        return Database;
    }

}
