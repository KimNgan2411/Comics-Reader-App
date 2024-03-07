package com.example.app;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class App extends Application {
    public static ArrayList<Comic> comics;
    public static ArrayList<Comic> getComic(Context context){
        if (comics == null) {
            DbHelper db = new DbHelper(context);
            comics = db.getAllComic();
        }
        return comics;

    }
 }
