package com.example.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    Switch aSwitch;
    TextView textView1, textView2;
    ArrayList<Comic> comics;

    static int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.Theme_Dark);
        }
        else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setting_activity);
        getSupportActionBar().setTitle("Cài Đặt");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation1);
        navigation.setOnItemSelectedListener(mOnItemSelectedListener);

        textView1 = findViewById(R.id.AboutUs);
        textView2 = findViewById(R.id.Mode);
        aSwitch= findViewById(R.id.Switch1);
        Intent intent = getIntent();
        comics = (ArrayList<Comic>) intent.getSerializableExtra("comics");
        if (flag == 1) aSwitch.setChecked(true);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SettingActivity.this, AboutUsActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    flag = 1;
                }
                else
                {
                    flag = 0;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

    }

    private BottomNavigationView.OnItemSelectedListener mOnItemSelectedListener= new BottomNavigationView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tags_list:
                    showFilterDialog();
                    return true;
                case R.id.Home:
                    onBackPressed();
                    return true;
                case R.id.Setting:
                    return true;
            }
            return false;
        }
    };

    private void showFilterDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
                    Intent intent = new Intent(SettingActivity.this, MyFilterActivity.class);
                    intent.putExtra("comics", comics);
                    intent.putExtra("filter_query", filter_query.toString());
                    startActivity(intent);
                }
                else
                {

                }
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
