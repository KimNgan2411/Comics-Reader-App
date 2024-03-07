package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class MyFavoriteActivity extends AppCompatActivity {
    RecyclerView rv ;
    ComicAdapter adapter;
    ArrayList<Comic> comics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Danh Sách Yêu Thích");
        setContentView(R.layout.activity_my_favorite);
        rv = findViewById(R.id.favorite_rv);
        Intent intent= getIntent();
        comics = (ArrayList<Comic>) intent.getSerializableExtra("comics");
        adapter = new ComicAdapter(comics);
        rv.setAdapter(adapter);
        adapter.getFilter().filter("FAVORITE");
        rv.setLayoutManager(new LinearLayoutManager(MyFavoriteActivity.this, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(MyFavoriteActivity.this, LinearLayoutManager.VERTICAL));
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("comics", comics);
        startActivity(i);
        return super.onSupportNavigateUp();

    }
}