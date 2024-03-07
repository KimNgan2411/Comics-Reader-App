package com.example.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class DbHelper {
    String DATABASE_NAME="COMIC.db";
    String tblname = "Truyen";
    String tblname2 = "TheLoai";
    Context context;


    public DbHelper(Context context) {
        this.context= context;
    }
    public SQLiteDatabase openDB(){
        return context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }
    public void closeDB(SQLiteDatabase db){
        db.close();
    }

    // Get comic
    public ArrayList<Comic> getAllComic(){
        ArrayList<Comic> tmp = new ArrayList<>();
        SQLiteDatabase db = openDB();
        String sql = "SELECT * FROM " + tblname;
        Cursor cursor = db.query("Truyen" , null, null , null , null, null, null ,null);

        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String name= cursor.getString(1);
            String author = cursor.getString(2);
            int IdCategory= cursor.getInt( 3);
            String des = cursor.getString(4);
            int isFavorite =  cursor.getInt(5);
            String imageLink = cursor.getString(6);
            int numbOfChap = cursor.getInt(7);
            tmp.add(new Comic(id,name, author, IdCategory, des, isFavorite, imageLink, numbOfChap));
        }
        closeDB(db);

        return tmp;
    }

    public ArrayList<ComicCategory> getAllCategory(){
        ArrayList<ComicCategory> tmp = new ArrayList<>();
        SQLiteDatabase db = openDB();
        Cursor cursor = db.query(tblname2 , null, null , null , null, null, null ,null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            tmp.add(new ComicCategory(id, name));
        }
        return tmp;
    }

    public String getCategoryOfComic(String id){
        String tmp = "";
        SQLiteDatabase db = openDB();
        Cursor c= db.rawQuery("SELECT NameCategory FROM TheLoai WHERE IdCategory=?", new String[]{id});
        if (c.moveToFirst())
        {
                tmp = c.getString(0);
        }
        return tmp;
    }

    public int updateFarvorite(String id, int isFavorite){
        SQLiteDatabase db = openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isFavorite", isFavorite);
        int tmp = db.update(tblname,contentValues,
                "Id=?",
                new String[]{String.valueOf(id)});
        closeDB(db);
        return tmp;
    }

    public ArrayList<Chapter> getAllUrl(String id)
    {
        SQLiteDatabase db = openDB();
        ArrayList<Chapter> tmp = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT Chap, Link FROM LinkTruyen WHERE Id = ? GROUP BY chap", new String[]{id});
        while (c.moveToNext()){
            String chap = c.getString(0);
            String url = c.getString(1);
            tmp.add(new Chapter(Integer.parseInt(chap), url));
        }
        return tmp;
    }

}
