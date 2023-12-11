package id.dimas.kasirpintar.module.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.model.Categories;
import id.dimas.kasirpintar.model.Products;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Categories> categoriesList;
    private List<Categories> filteredList; // Add a filtered list
    private List<Products> productList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public CategoryAdapter(Context context, List<Categories> categoriesList, OnItemClickListener listener) {
        this.categoriesList = categoriesList;
        this.context = context;
        this.onItemClickListener = listener;
        this.filteredList = new ArrayList<>(categoriesList); // Initialize filteredList with the full list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view, context, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories product = filteredList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Update the filtered list with the full list
    public void resetFilter() {
        filteredList.clear();
        filteredList.addAll(categoriesList);
        notifyDataSetChanged();
    }

    // Set a new filtered list and update the adapter
    public void setFilter(List<Categories> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCategoryName;
        private ImageView ivDelete;
        private OnItemClickListener itemClickListener;


        ViewHolder(@NonNull View itemView, Context context, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.itemClickListener = onItemClickListener;
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }

        void bind(Categories categories) {
            tvCategoryName.setText(categories.getName());
            ivDelete.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onDeleteClick(categories);
                }
            });
            // Add any other binding logic for categories details
        }
    }


    public interface OnItemClickListener {
        void onEditClick(Categories categories);

        void onDeleteClick(Categories categories);
    }

}