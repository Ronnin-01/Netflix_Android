package com.application.netflix.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.netflix.Model.AllCategory;
import com.application.netflix.Model.CategoryItemList;
import com.application.netflix.R;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>{

    Context context;
    List<AllCategory> allcategoryLists;

    public MainRecyclerAdapter(Context context, List<AllCategory> allcategoryLists) {
        this.context = context;
        this.allcategoryLists = allcategoryLists;
    }

    @NonNull
    @Override
    public MainRecyclerAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MainRecyclerAdapter.MainViewHolder(LayoutInflater.from(context).inflate(R.layout.mainrecyclerlayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerAdapter.MainViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.categoryname.setText(allcategoryLists.get(position).getCategoryTitle());
        setItemRecycler(holder.itemRecycler,allcategoryLists.get(position).getCategoryItemList());
        
    }

    @Override
    public int getItemCount() {
        return allcategoryLists.size();
    }

    public static final class MainViewHolder extends RecyclerView.ViewHolder{
        TextView categoryname;
        RecyclerView itemRecycler;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryname=itemView.findViewById(R.id.item_category);
            itemRecycler=itemView.findViewById(R.id.item_recycler);
        }
    }
    public void setItemRecycler(RecyclerView recyclerView, List<CategoryItemList> categoryItem){
        ItemRecyclerAdapter itemRecyclerAdapter = new ItemRecyclerAdapter(context,categoryItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(itemRecyclerAdapter);
    }
}
