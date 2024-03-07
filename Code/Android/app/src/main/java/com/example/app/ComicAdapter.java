package com.example.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicVH> implements Filterable {
    ArrayList<Comic> comics;
    ArrayList<Comic> comicold;

    public ComicAdapter( ArrayList<Comic> comics) {
        this.comics = comics;
        this.comicold= comics;
    }

    @NonNull
    @Override
    public ComicVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_row, parent, false);
        return new ComicVH(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ComicVH holder, int position) {
        ComicVH Myviewholder= (ComicVH) holder;
        Comic comic= comics.get(position);
        Glide.with(holder.imgComic.getContext()).load(comic.getImageLink()).into(holder.imgComic);
        holder.txName.setText(comic.getName());
        holder.txAuthor.setText(comic.getAuthor());
        holder.txDesc.setText(comic.getDescription());
        if (comic.getIsfavorite() == 1){
            holder.imgFav.setImageResource(R.drawable.like);
        }
        else
            holder.imgFav.setImageResource(R.drawable.unlike);
        holder.txName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Chitiet.class);
                intent.putExtra("id",comic.getId());
                view.getContext().startActivity(intent);
            }
        });

        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comic.getIsfavorite() == 1)
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                    alertDialog.setTitle("Xác nhận");
                    alertDialog.setMessage("Bạn có muốn xóa truyện khỏi yêu thích?");
                    alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            comic.setIsfavorite(0);
                            DbHelper db = new DbHelper(view.getContext());
                            db.updateFarvorite(comic.getId(), 0);
                            holder.imgFav.setImageResource(R.drawable.unlike);
                            notifyDataSetChanged();
                        }
                    });
                    alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();

                }
                else
                {
                    comic.setIsfavorite(1);
                    DbHelper db = new DbHelper(view.getContext());
                    db.updateFarvorite(comic.getId(), 1);
                    holder.imgFav.setImageResource(R.drawable.like);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    class ComicVH extends RecyclerView.ViewHolder{
        ImageView imgComic, imgFav;
        TextView txName, txAuthor, txDesc;
        public ComicVH(@NonNull View itemView) {
            super(itemView);
            imgComic= itemView.findViewById(R.id.imgComic);
            imgFav= itemView.findViewById(R.id.imgFav);
            txName= itemView.findViewById(R.id.txName);
            txAuthor= itemView.findViewById(R.id.txAuthor);
            txDesc= itemView.findViewById(R.id.txDesc);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch= charSequence.toString();
                if (strSearch.isEmpty()){
                    comics= comicold;
                }
                if (strSearch.equals("FAVORITE")){
                    ArrayList<Comic> list= new ArrayList<Comic>();
                    for (Comic comic: comics) {
                       if(comic.getIsfavorite() == 1){
                           list.add(comic);
                       }
                    }
                    comics= list;
                }
                else{
                    ArrayList<Comic> list= new ArrayList<Comic>();
                    for (Comic comic: comicold) {
                        if(comic.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(comic);
                        }
                    }
                    comics= list;
                }
                FilterResults filterresults= new FilterResults();
                filterresults.values= comics;
                return filterresults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                comics = (ArrayList<Comic>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
