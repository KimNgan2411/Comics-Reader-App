package com.example.app;

import java.io.Serializable;

public class ComicCategory implements Serializable {
    int id;
    String nameTag;

    public ComicCategory(int id, String nameTag) {
        this.id = id;
        this.nameTag = nameTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameTag() {
        return nameTag;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }
}
