package id.dimas.kasirpintar.module.settings;

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
import id.dimas.kasirpintar.model.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<Users> usersList;
    private List<Users> filteredList; // Add a filtered list
    Context context;
    private OnItemClickListener onItemClickListener;

    public UserAdapter(Context context, List<Users> usersList, OnItemClickListener listener) {
        this.usersList = usersList;
        this.context = context;
        this.onItemClickListener = listener;
        this.filteredList = new ArrayList<>(usersList); // Initialize filteredList with the full list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view, context, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users product = filteredList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Update the filtered list with the full list
    public void resetFilter() {
        filteredList.clear();
        filteredList.addAll(usersList);
        notifyDataSetChanged();
    }

    // Set a new filtered list and update the adapter
    public void setFilter(List<Users> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView contentName;
        private TextView contentEmail;
        private ImageView ivDelete;
        private OnItemClickListener itemClickListener;

        ViewHolder(@NonNull View itemView, Context context, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.itemClickListener = onItemClickListener;
            contentName = (TextView) itemView.findViewById(R.id.contentName);
            contentEmail = (TextView) itemView.findViewById(R.id.contentEmail);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);

        }

        void bind(Users user) {
            String name = user.getName();
            if (user.isAdmin()) {
                name += "(Admin)";
                ivDelete.setVisibility(View.GONE);
            }

            contentName.setText(name);
            contentEmail.setText(user.getEmail());
            ivDelete.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onDeleteClick(user);
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onEditClick(Users users);

        void onDeleteClick(Users users);
    }

}