package id.dimas.kasirpintar.module.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.model.Products;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Products> productList;
    Context context;
    private List<Products> filteredList;
    private OnItemClickListener onItemClickListener;

    public ProductAdapter(Context context, List<Products> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.context = context;
        this.onItemClickListener = listener;
        this.filteredList = new ArrayList<>(productList); // Initialize filteredList with the full list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view, context, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = filteredList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Update the filtered list with the full list
    public void resetFilter() {
        filteredList.clear();
        filteredList.addAll(productList);
        notifyDataSetChanged();
    }

    // Set a new filtered list and update the adapter
    public void setFilter(List<Products> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productNameTextView;
        private TextView contentQty;
        private TextView contentTotal;
        private ImageView ivProduct;
        private ImageView ivDelete;
        Context context;
        DecimalFormat decimalFormat;
        private OnItemClickListener itemClickListener;


        ViewHolder(@NonNull View itemView, Context context, OnItemClickListener listener) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.contentName);
            contentQty = itemView.findViewById(R.id.contentQty);
            contentTotal = itemView.findViewById(R.id.contentTotal);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            decimalFormat = new DecimalFormat("#,###.##");
            this.context = context;
            this.itemClickListener = listener;
        }

        void bind(Products product) {
            productNameTextView.setText(product.getName());
            contentQty.setText(String.format(": %s", product.getStock()));
            contentTotal.setText(String.format("Rp %s", decimalFormat.format(Double.parseDouble(product.getSellPrice()))).replace(",", "."));
            Glide.with(context)
                    .load(product.image)
                    .placeholder(R.drawable.ic_upload) // Optional placeholder
                    .error(R.drawable.ic_upload) // Optional error placeholder
                    .into(ivProduct);

            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onEditClick(product);
                }
            });
            ivDelete.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onDeleteClick(product);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onEditClick(Products products);

        void onDeleteClick(Products products);
    }

}