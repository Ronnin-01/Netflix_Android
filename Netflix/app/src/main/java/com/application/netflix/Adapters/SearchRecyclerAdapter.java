package com.application.netflix.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.netflix.Model.AllCategory;
import com.application.netflix.Model.CategoryItemList;
import com.application.netflix.R;
import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder> implements Filterable {
    Context context;
    List<AllCategory> allcategoryLists;
    List<AllCategory>fulllist;

    public SearchRecyclerAdapter(Context context, List<AllCategory> allcategoryLists) {
        this.context = context;
        this.allcategoryLists = allcategoryLists;
        this.fulllist = new ArrayList<>(allcategoryLists);
    }

    public static final class SearchViewHolder extends RecyclerView.ViewHolder{

        RecyclerView itemRecycler;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRecycler=itemView.findViewById(R.id.search_recycler);
        }
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AllCategory> filteredList= new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0 ){
                filteredList.addAll(allcategoryLists);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (AllCategory item : fulllist) {
                    if (item.getCategoryTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results =new FilterResults();
            results.values=filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            allcategoryLists.clear();
            allcategoryLists.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public SearchRecyclerAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchRecyclerAdapter.SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.searchrecyclerlayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerAdapter.SearchViewHolder holder, int position) {
        setItemRecycler(holder.itemRecycler,allcategoryLists.get(position).getCategoryItemList());
    }

    @Override
    public int getItemCount() {
        return allcategoryLists.size();
    }
    public void setItemRecycler(RecyclerView recyclerView, List<CategoryItemList> categoryItem) {
        ItemRecyclerAdapter itemRecyclerAdapter = new ItemRecyclerAdapter(context, categoryItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(itemRecyclerAdapter);
    }
}
