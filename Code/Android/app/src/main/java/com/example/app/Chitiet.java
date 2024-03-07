package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Chitiet extends AppCompatActivity {
    TextView detailname, detailauthor, detaildesc, detailtags;
    ImageView detailimg;
    Comic comic;
    private RecyclerView tagList;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        getSupportActionBar().setTitle("Chi Tiết");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        for(Comic item:App.getComic(this)){
            if(item.getId().equals(id))
                comic = item;
        }
        DbHelper db = new DbHelper(this);
        if(comic != null){
            detailname.setText(comic.name);
            detailauthor.setText(comic.author);
            detaildesc.setText(comic.description);
            Glide.with(detailimg.getContext()).load(comic.getImageLink()).into(detailimg);
        }

        tagList = findViewById(R.id.tag_list);
        tagList.setHasFixedSize(true);
        tagList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ArrayList<String> tags = new ArrayList<>();
        String temp=  String.valueOf(comic.getIdCategory());
        tags.add(db.getCategoryOfComic(temp));
        tagList.setAdapter(new TagAdapter(tags));
        findViewById(R.id.all_chapters).setOnClickListener(v -> setUpChapterSheet());


    }

    private void setUpChapterSheet() {
        Intent intent = new Intent(Chitiet.this, ChapterSheet.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void initView(){
        detailname = findViewById(R.id.detail_name);
        detailauthor = findViewById(R.id.detail_author);
        detaildesc = findViewById(R.id.detail_des);
        detailimg = findViewById(R.id.detail_ava);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}