package com.example.app;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class MyFilterActivity extends AppCompatActivity {
    RecyclerView rv ;
    ComicAdapter adapter;
    ArrayList<Comic> comics, comics_filter;
    String comic_query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_my_filter);

        rv = findViewById(R.id.filter_rv);
        Intent intent= getIntent();
        comics = (ArrayList<Comic>) intent.getSerializableExtra("comics");
        comic_query = (String) intent.getSerializableExtra("filter_query");
        comics_filter = new ArrayList<>();
        for (Comic item: comics)
        {
            if (checkCategory(item.getIdCategory(), comic_query) == 1)
                comics_filter.add(item);
        }
        adapter = new ComicAdapter(comics_filter);
        rv.setAdapter(adapter);
        adapter.getFilter().filter("");
        rv.setLayoutManager(new LinearLayoutManager(MyFilterActivity.this, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(MyFilterActivity.this, LinearLayoutManager.VERTICAL));
    }

    public int checkCategory(int IdCategory, String tagFilter)
    {
        int temp = 0;
        DbHelper db = new DbHelper(this);
        ArrayList<ComicCategory> categories =  db.getAllCategory();
        for (ComicCategory item: categories)
        {
            if (item.getId() == IdCategory)
                if (tagFilter.contains(item.getNameTag()))
                    temp = 1;

        }
        return temp;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}