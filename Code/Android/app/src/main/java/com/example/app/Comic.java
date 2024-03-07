package com.example.app;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Comic implements Serializable, Comparable<Comic> {
    String id;
    String name;
    String author;
    int idCategory;
    String description;
    int isfavorite;
    String imageLink;
    int numOfChapter;

    public Comic(String id, String name, String author, int idCategory, String description, int isfavorite, String imageLink, int numOfChapter) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.idCategory = idCategory;
        this.description = description;
        this.isfavorite = isfavorite;
        this.imageLink = imageLink;
        this.numOfChapter = numOfChapter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(int isfavorite) {
        this.isfavorite = isfavorite;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getNumOfChapter() {
        return numOfChapter;
    }

    public void setNumOfChapter(int numOfChapter) {
        this.numOfChapter = numOfChapter;
    }

//    @SuppressLint("SuspiciousIndentation")
//    public String getDescription() {
//        if (description.length()>120)
//        {
//            return description.substring(0, 121) + "...";
//        }
//        else
//        return description;
//    }


    @Override
    public int compareTo(Comic comic) {
        return name.compareToIgnoreCase(comic.name);
    }
}
