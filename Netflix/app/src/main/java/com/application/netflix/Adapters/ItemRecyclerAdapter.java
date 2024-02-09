package com.application.netflix.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.netflix.Mainscreens.MovieDetails;
import com.application.netflix.Model.CategoryItemList;
import com.application.netflix.R;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {
    Context context;
    List<CategoryItemList>categoryItemLists;
    List<CategoryItemList>moviesListAll;

    public ItemRecyclerAdapter(Context context, List<CategoryItemList> categoryItemLists) {
        this.context = context;
        this.categoryItemLists = categoryItemLists;
        this.moviesListAll = new ArrayList<>(categoryItemLists);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.category_row_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(categoryItemLists.get(position).getImageUrl()).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MovieDetails.class);
                i.putExtra("movieId",categoryItemLists.get(position).getId());
                i.putExtra("movieName",categoryItemLists.get(position).getMovieName());
                i.putExtra("movieImageUrl",categoryItemLists.get(position).getImageUrl());
                i.putExtra("movieFile",categoryItemLists.get(position).getFileUrl());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryItemLists.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.item_image);
        }
    }
}
