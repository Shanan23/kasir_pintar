package id.dimas.kasirpintar.module.cart;

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

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.Products;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Products> filteredList;
    private List<Products> cartList;
    Context context;
    private List<OrdersDetail> orderDetails;
    private OnItemClickListener listener;
    private SharedPreferenceHelper sharedPreferenceHelper;

    public CartAdapter(Context context, List<Products> cartList, OnItemClickListener listener) {
        this.cartList = cartList;
        this.context = context;
        this.listener = listener;
        this.filteredList = new ArrayList<>(cartList); // Initialize filteredList with the full list
        sharedPreferenceHelper = new SharedPreferenceHelper(context);

    }


    // Constructor and other methods go here

    public interface OnItemClickListener {
        void onItemClick(List<OrdersDetail> ordersDetailList);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView contentCalendar;
        TextView contentQty;
        TextView contentTotal;
        private ImageView ivProduct;
        private ImageView ivDelete;
        DecimalFormat decimalFormat;
        private OnItemClickListener itemClickListener;
        Context context;
        private List<Products> cartList;
        private AppDatabase appDatabase;

        public ViewHolder(View itemView, Context context, List<Products> cartList, OnItemClickListener itemClickListener) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.contentName);
            contentCalendar = itemView.findViewById(R.id.contentCalendar);
            contentQty = itemView.findViewById(R.id.contentQty);
            contentTotal = itemView.findViewById(R.id.contentTotal);
            contentQty = itemView.findViewById(R.id.contentQty);
            contentTotal = itemView.findViewById(R.id.contentTotal);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            decimalFormat = new DecimalFormat("#,###.##");
            this.context = context;
            this.itemClickListener = itemClickListener;
            this.cartList = cartList;
            appDatabase = MyApp.getAppDatabase();
            orderDetails = new ArrayList<>();

        }

        void bind(Products product) {
            tvItemName.setText(product.getName());
            contentQty.setText(String.format(": %s", product.getQty()));
            contentTotal.setText(String.format("Rp %s", decimalFormat.format(Double.parseDouble(product.getSellPrice()))).replace(",", "."));
            Glide.with(context)
                    .load(product.image)
                    .placeholder(R.drawable.ic_upload) // Optional placeholder
                    .error(R.drawable.ic_upload) // Optional error placeholder
                    .into(ivProduct);

            itemView.setOnClickListener(v -> {
                increaseQuantity();
                itemClickListener.onItemClick(orderDetails);
            });
            ivDelete.setOnClickListener(v -> {
                clearQuantity();
                itemClickListener.onItemClick(orderDetails);

            });
        }

        public void increaseQuantity() {
            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Products currentProduct = cartList.get(position);

                // Check if the product is already in orderDetails
                boolean productExists = false;
                int existingPosition = -1;

                for (int i = 0; i < orderDetails.size(); i++) {
                    if (orderDetails.get(i).getProducts().getId() == currentProduct.getId()) {
                        productExists = true;
                        existingPosition = i;
                        break;
                    }
                }

                // If the product is already in orderDetails, update the quantity
                if (productExists) {
                    OrdersDetail existingOrderDetail = orderDetails.get(existingPosition);
                    int updatedQty = existingOrderDetail.getQty() + 1;
                    int currentStock = cartList.get(position).getStock();
                    if (Boolean.parseBoolean(currentProduct.getIsStock())) {
                        if (updatedQty > currentProduct.getStock()) {
                            return;
                        }
                    }
                    cartList.get(position).setStock(currentStock - 1);
                    cartList.get(position).setQty(updatedQty);
                    currentProduct.setStock(currentStock - 1);
                    new Thread(() -> {
                        currentProduct.setIdOutlet(sharedPreferenceHelper.getShopId());
                        appDatabase.productsDao().upsertProducts(currentProduct);
                    }).start();
                    existingOrderDetail.setQty(updatedQty);
                    existingOrderDetail.setTotalDetails(updatedQty * Integer.parseInt(existingOrderDetail.getProducts().getSellPrice()));
                    orderDetails.set(existingPosition, existingOrderDetail);
                    notifyItemChanged(position);
                } else {
                    // If the product is not in orderDetails, add a new orderDetail
                    OrdersDetail newOrderDetail = new OrdersDetail();
                    int currentStock = cartList.get(position).getStock();
                    if (Boolean.parseBoolean(currentProduct.getIsStock())) {
                        if (1 > currentProduct.getStock()) {
                            return;
                        }
                    }
                    currentProduct.setQty(1);
                    currentProduct.setStock(currentStock - 1);
                    new Thread(() -> {
                        currentProduct.setIdOutlet(sharedPreferenceHelper.getShopId());
                        appDatabase.productsDao().upsertProducts(currentProduct);
                    }).start();
                    cartList.get(position).setStock(currentStock - 1);
                    cartList.get(position).setQty(1);
                    newOrderDetail.setProducts(currentProduct);
                    newOrderDetail.setItemId(String.valueOf(currentProduct.getId()));
                    newOrderDetail.setName(currentProduct.getName());
                    newOrderDetail.setTotalDetails(1 * Integer.parseInt(currentProduct.getSellPrice()));
                    newOrderDetail.setQty(1);
                    orderDetails.add(newOrderDetail);
                    notifyItemChanged(position);

                }
            }
        }

        public void clearQuantity() {
            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Products currentProduct = cartList.get(position);

                // Check if the product is already in orderDetails
                int existingPosition = -1;
                for (int i = 0; i < orderDetails.size(); i++) {
                    if (orderDetails.get(i).getProducts().getId() == currentProduct.getId()) {
                        existingPosition = i;
                        break;
                    }
                }

                // If the product is in orderDetails, remove it
                if (existingPosition != -1) {
                    orderDetails.remove(existingPosition);
                }

                // Update the quantity to 0 in the cartList
                int currentQty = 0;
                cartList.get(position).setStock(cartList.get(position).getQty() + cartList.get(position).getStock());
                currentProduct.setStock(cartList.get(position).getQty() + cartList.get(position).getStock());
                new Thread(() -> {
                    currentProduct.setIdOutlet(sharedPreferenceHelper.getShopId());
                    appDatabase.productsDao().upsertProducts(currentProduct);
                }).start();
                cartList.get(position).setQty(currentQty);
                currentProduct.setQty(currentQty);
                notifyItemChanged(position);
            }
        }
    }

    public void setFilter(List<Products> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view, context, cartList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products currentCart = cartList.get(position);
        holder.bind(currentCart);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public List<OrdersDetail> getOrderDetails() {
        return orderDetails;
    }
}