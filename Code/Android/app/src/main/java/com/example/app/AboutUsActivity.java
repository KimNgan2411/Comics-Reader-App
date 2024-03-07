package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class AboutUsActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_aboutus);
        textView = findViewById(R.id.AboutUs);
        InputStream is = getResources().openRawResource(R.raw.infor);
        try {
            String data = ReadText(is);
            textView.setText(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String ReadText(InputStream is) throws IOException {
        is = this.getResources().openRawResource(R.raw.infor);
        byte[] buffer = new byte[is.available()];
        while (is.read(buffer) != -1);
        String jsontext = new String(buffer);
        return jsontext;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    }


