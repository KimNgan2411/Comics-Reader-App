package com.example.app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChapterSheetAdapter extends RecyclerView.Adapter<ChapterSheetAdapter.MyViewHolder> {
    private ArrayList<Chapter> chaptersList;

    public ChapterSheetAdapter(ArrayList<Chapter> chaptersList)
    {
        this.chaptersList= chaptersList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nameChap;
        private LinearLayout layout;
        public MyViewHolder(final View view){
            super(view);
            nameChap = view.findViewById(R.id.numChap);
            layout = view.findViewById(R.id.itemChapter);
        }
    }
    @NonNull
    @Override
    public ChapterSheetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterSheetAdapter.MyViewHolder holder, int position) {
        int name = chaptersList.get(position).getNumber();
        String url = chaptersList.get(position).getUrl();
        holder.nameChap.setText(String.valueOf(name));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(holder.nameChap.getContext(), ChapterActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("chap", String.valueOf(name));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chaptersList.size();
    }
}
