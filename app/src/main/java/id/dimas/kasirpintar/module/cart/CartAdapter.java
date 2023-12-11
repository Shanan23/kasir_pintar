package id.dimas.kasirpintar.module.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.Products;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public CartAdapter(List<OrdersDetail> cartList, OnItemClickListener listener) {
        this.cartList = cartList;
        this.listener = listener;
    }

    private List<OrdersDetail> cartList;
    private OnItemClickListener listener;

    // Constructor and other methods go here

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView contentCalendar;
        TextView contentQty;
        TextView contentTotal;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            contentCalendar = itemView.findViewById(R.id.contentCalendar);
            contentQty = itemView.findViewById(R.id.contentQty);
            contentTotal = itemView.findViewById(R.id.contentTotal);
        }
    }

    public void increaseQuantity(int position) {
        OrdersDetail product = cartList.get(position);
        product.setQty(product.getQty() + 1);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrdersDetail currentCart = cartList.get(position);
        // Assuming Product class has methods like getItemName and getQuantity
        String itemText = currentCart.getQty() + " x " + currentCart.getProducts().getName();
        holder.tvItemName.setText(itemText);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}