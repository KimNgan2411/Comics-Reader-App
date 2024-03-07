package com.example.app;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    RecyclerView rvComics;
    ArrayList<Comic> comics;
    ArrayList<Comic> comicolds;
    ComicAdapter comicAdapter;
    SearchView searchView;
    int t=0;
    private ActionBar toolbar;



    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            comicAdapter.notifyDataSetChanged();
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.Theme_Dark);
        }
        else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        processCopy();
        setContentView(R.layout.activity_main);
        rvComics = findViewById(R.id.rvComics);
        Intent temp = getIntent();
        comics =  (ArrayList<Comic>) temp.getSerializableExtra("comics");
        if (comics == null)
            comics = App.getComic(this);
        comicAdapter = new ComicAdapter(comics);
        rvComics.setAdapter(comicAdapter);

        rvComics.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        rvComics.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));

        //bottom navigation bar
        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(mOnItemSelectedListener);

    }


    private void processCopy() {
        File dbFile = getDatabasePath("COMIC.db");
        if (!dbFile.exists()) { try{CopyDataBaseFromAsset(); 
            Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show(); } 
        catch (Exception e){ Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show(); } }
    }


    private void CopyDataBaseFromAsset() {
        try { InputStream myInput; myInput = getAssets().open("COMIC.db"); String outFileName = getDatabasePath();
            File f = new File(getApplicationInfo().dataDir + "/databases/");
            if (!f.exists()) f.mkdir(); OutputStream myOutput = new FileOutputStream(outFileName);
            int size = myInput.available(); byte[] buffer = new byte[size]; myInput.read(buffer); myOutput.write(buffer); myOutput.flush();
            myOutput.close(); myInput.close(); } catch (IOException e) { e.printStackTrace(); } }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + "/databases/"+ "COMIC.db";
    }


    private BottomNavigationView.OnItemSelectedListener mOnItemSelectedListener= new BottomNavigationView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tags_list:
                    showFilterDialog();
                    return true;
                case R.id.Home:
                    toolbar.setTitle("Trang Chủ");
                    return true;
                case R.id.Setting:
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    intent.putExtra("comics" ,comics);
                    mGetContent.launch(intent);
                    return true;
            }
            return false;
        }
    };

    private void showFilterDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Chọn thể loại");

        LayoutInflater inflater = this.getLayoutInflater();
        View filter_layout = inflater.inflate(R.layout.dialog_options, null);
        AutoCompleteTextView txt_category = (AutoCompleteTextView)filter_layout.findViewById(R.id.txt_category);
        ChipGroup chipGroup= (ChipGroup) filter_layout.findViewById(R.id.chipGroup);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, Common.categories);
        txt_category.setAdapter(adapter);
        txt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                txt_category.setText("");
                Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                chip.setText(((TextView)view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chipGroup.removeView(view);
                    }
                });
                chipGroup.addView(chip);
            }
        });
        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("Cancel", ((dialogInterface, i) -> {dialogInterface.dismiss();}));
        alertDialog.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query =  new StringBuilder("");
                for (int j=0 ; j< chipGroup.getChildCount(); j++)
                {
                    Chip chip = (Chip) chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }
                Collections.sort(filter_key);
                for (String key:filter_key)
                {
                    filter_query.append(key).append(",");
                }
                //Remove last ","
                if (filter_query.length() != 0) {
                    filter_query.setLength(filter_query.length() - 1);

                    //Xử lí
                    Intent intent = new Intent(MainActivity.this, MyFilterActivity.class);
                    intent.putExtra("comics", comics);
                    intent.putExtra("filter_query", filter_query.toString());
                    mGetContent.launch(intent);
                }
                else
                {

                }
            }
        });
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.list_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                comicAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                comicAdapter.getFilter().filter(newText);
                return false;
            }
        });
      return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        t++;
        if(item.getItemId()==R.id.fav_list){
            Intent intent = new Intent(MainActivity.this,MyFavoriteActivity.class);
            intent.putExtra("comics", comics);
            mGetContent.launch(intent);
            comicAdapter.notifyDataSetChanged();
            this.finish();
        }
        if((item.getItemId()==R.id.action_sort)){
            if (t!= 0)
            Collections.sort(App.getComic(this), new Comparator<Comic>() {
                @Override
                public int compare(Comic comic, Comic t1) {
                   switch(t){
                       case 1:
                           comicolds = new ArrayList<Comic>(comics);
                           return (comic.name.compareTo(t1.name));
                       case 2:
                           return (t1.name.compareTo(comic.name));
                       default:
                           t = 1;
                           return 0;
                   }
                }
            });
            else
            {
                    comics = new ArrayList<Comic>(comicolds);
                    comicAdapter.notifyDataSetChanged();

            }
            comicAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }


}
