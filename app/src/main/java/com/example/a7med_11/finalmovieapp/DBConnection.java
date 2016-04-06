package com.example.a7med_11.finalmovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by a7med_11 on 29/01/16.
 */
public class DBConnection {

    DBinfo obj;
    public DBConnection(Context context) {
        obj = new DBinfo(context);

    }

    public long dataInsert(String poster, String backposter,String title,String disc,String releasedate,String voteaverage,String movieid) {
        SQLiteDatabase sqLiteDatabase = obj.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(obj.posterpath,poster);
        contentValues.put(obj.backdroppath,backposter);
        contentValues.put(obj.title,title);
        contentValues.put(obj.disc,disc);
        contentValues.put(obj.releasedata,releasedate);
        contentValues.put(obj.voteaverage,voteaverage);
        contentValues.put(obj.movieid,movieid);


        long id = sqLiteDatabase.insert(obj.tableName,null,contentValues);
        return id;

    }

    public boolean search_movie(String id){


        ArrayList<String[]> datasql = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = obj.getReadableDatabase();
        String[] columns = {obj.UID,obj.posterpath,obj.backdroppath,obj.title,obj.disc,obj.releasedata,obj.voteaverage,obj.movieid};
        Cursor cursor = sqLiteDatabase.query(obj.tableName,columns,null,null,null,null,null);

        String myid;

        while (cursor.moveToNext()){
            myid = cursor.getString(7);
            if(myid.equals(id))
                return true;
        }

        return false;
    }

    public int delete(String id){
        SQLiteDatabase sqLiteDatabase = obj.getReadableDatabase();
        String [] whereArgs = {id};
        String s = obj.movieid + " =? ";
        int count = sqLiteDatabase.delete(obj.tableName,s,whereArgs);

        return count;
    }

    public ArrayList<String[]> viewData(){
        ArrayList<String[]> datasql = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = obj.getReadableDatabase();
        String[] columns = {obj.UID,obj.posterpath,obj.backdroppath,obj.title,obj.disc,obj.releasedata,obj.voteaverage,obj.movieid};
        Cursor cursor = sqLiteDatabase.query(obj.tableName,columns,null,null,null,null,null);

        String[] myarr = new String[7];
        while (cursor.moveToNext()){
            myarr[0] = cursor.getString(1);
            myarr[1] = cursor.getString(2);
            myarr[2] = cursor.getString(3);
            myarr[3] = cursor.getString(4);
            myarr[4] = cursor.getString(5);
            myarr[5] = cursor.getString(6);
            myarr[6] = cursor.getString(7);


            datasql.add(myarr);
            myarr = new String[7];
        }

        return datasql;
    }


    static class DBinfo extends SQLiteOpenHelper {

        private static final String dataBase_Name = "MovieAppdb";
        private static final String tableName = "Favourites";
        private static final int dataBase_Version = 2;
        private static final String UID = "id";
        private static final String posterpath = "PosterPath";
        private static final String backdroppath = "BackDropPath";
        private static final String title = "Title";
        private static final String disc = "Description";
        private static final String releasedata = "ReleaseDate";
        private static final String voteaverage = "VoteAverage";
        private static final String movieid = "MovieID";


        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + tableName;
        private static final String CREATE_TABLE = "CREATE TABLE " + tableName +
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+posterpath+" VARCHAR(255), "+backdroppath+" VARCHAR(255), "+title+" VARCHAR(255), "+disc+" VARCHAR(255), "+releasedata+" VARCHAR(255), "+voteaverage+" VARCHAR(255), "+movieid+" VARCHAR(255));";



        private Context context;


        public DBinfo(Context context) {
            super(context, dataBase_Name, null, dataBase_Version);
            this.context = context;
            //Toast.makeText(context, "this Constructor", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE);
           // Toast.makeText(context,"OnCreate Method" ,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(DROP_TABLE);
            onCreate(db);
            //Toast.makeText(context,"OnUpgrade Method" ,Toast.LENGTH_LONG).show();

        }
    }

}