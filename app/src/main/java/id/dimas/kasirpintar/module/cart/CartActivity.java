package id.dimas.kasirpintar.module.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import id.dimas.kasirpintar.R;

public class CartActivity extends AppCompatActivity {

    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new CartAdapter(productList);
        recyclerView.setAdapter(adapter);

        // Handle item clicks
        adapter.setOnItemClickListener(position -> {
            // Handle item click here
            // You can update the quantity or perform any other action
            // For example, you can open a dialog to change the quantity
            adapter.increaseQuantity(position);
        });
    }
}