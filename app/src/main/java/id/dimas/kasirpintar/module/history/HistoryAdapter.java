package id.dimas.kasirpintar.module.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.model.Orders;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Orders> ordersList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public HistoryAdapter(Context context, List<Orders> ordersList, OnItemClickListener listener) {
        this.ordersList = ordersList;
        this.context = context;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_transaction, parent, false);
        return new ViewHolder(view, context, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Orders orders = ordersList.get(position);
        holder.bind(orders);
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout clHistory;
        private OnItemClickListener itemClickListener;
        private View v1;
        private TextView tvItemName;
        private View v2;
        private ImageView lblCalendar;
        private TextView contentCalendar;
        private TextView lblQty;
        private TextView contentQty;
        private TextView lblTotal;
        private TextView contentTotal;


        ViewHolder(@NonNull View itemView, Context context, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.itemClickListener = onItemClickListener;

            clHistory = itemView.findViewById(R.id.clHistory);
            v1 = itemView.findViewById(R.id.v1);
            tvItemName = itemView.findViewById(R.id.contentName);
            v2 = itemView.findViewById(R.id.v2);
            lblCalendar = itemView.findViewById(R.id.lblCalendar);
            contentCalendar = itemView.findViewById(R.id.contentCalendar);
            lblQty = itemView.findViewById(R.id.lblQty);
            contentQty = itemView.findViewById(R.id.contentQty);
            lblTotal = itemView.findViewById(R.id.lblTotal);
            contentTotal = itemView.findViewById(R.id.contentTotal);

        }

        void bind(Orders orders) {
            tvItemName.setText(String.format("#000%d", orders.getId()));
            contentCalendar.setText(orders.getOrderDate());
            contentQty.setText(String.valueOf(orders.getProfitItem().getTotalItem()));
            contentTotal.setText(String.valueOf(orders.getAmount()));


            clHistory.setOnClickListener(v -> {
                itemClickListener.onEditClick(orders);
            });
        }
    }


    public interface OnItemClickListener {
        void onEditClick(Orders orders);

        void onDeleteClick(Orders orders);
    }

}