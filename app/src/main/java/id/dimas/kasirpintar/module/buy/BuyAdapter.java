package id.dimas.kasirpintar.module.buy;

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
import id.dimas.kasirpintar.model.Buy;

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.ViewHolder> {

    private List<Buy> buyList;
    private List<Buy> filteredList; // Add a filtered list
    Context context;
    private OnItemClickListener onItemClickListener;

    public BuyAdapter(Context context, List<Buy> buyList, OnItemClickListener listener) {
        this.buyList = buyList;
        this.context = context;
        this.onItemClickListener = listener;
        this.filteredList = new ArrayList<>(buyList); // Initialize filteredList with the full list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy, parent, false);
        return new ViewHolder(view, context, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Buy product = filteredList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Update the filtered list with the full list
    public void resetFilter() {
        filteredList.clear();
        filteredList.addAll(buyList);
        notifyDataSetChanged();
    }

    // Set a new filtered list and update the adapter
    public void setFilter(List<Buy> filteredList) {
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

        void bind(Buy buy) {
            tvCategoryName.setText(buy.getName());
            ivDelete.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onDeleteClick(buy);
                }
            });
            // Add any other binding logic for categories details
        }
    }


    public interface OnItemClickListener {
        void onEditClick(Buy buy);

        void onDeleteClick(Buy buy);
    }

}