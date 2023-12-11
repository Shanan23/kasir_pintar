package id.dimas.kasirpintar.module.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.dao.ProductsDao;
import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.Products;

public class CartActivity extends AppCompatActivity {

    private List<Products> cartList;
    private CartAdapter cartAdapter;
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;

    private TextView contentQtyTotal;
    private TextView contentTotal;
    private TextView contentProfit;
    private CardView cvPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        contentQtyTotal = findViewById(R.id.contentQtyTotal);
        contentTotal = findViewById(R.id.contentTotal);
        contentProfit = findViewById(R.id.contentProfit);
        cvPay = findViewById(R.id.cvPay);

        recyclerView = findViewById(R.id.rvCart);
        appDatabase = MyApp.getAppDatabase();

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList, ordersDetailList -> {
            int totalQty = 0;
            int totalPrice = 0;
            int totalProfit = 0;
            for (OrdersDetail ordersDetail : ordersDetailList
            ) {
                int sellPrice = ordersDetail.getProducts().getQty() * Integer.parseInt(ordersDetail.getProducts().getSellPrice());
                int buyPrice = ordersDetail.getProducts().getQty() * Integer.parseInt(ordersDetail.getProducts().getBuyPrice());
                totalQty += ordersDetail.getProducts().getQty();
                totalProfit += (sellPrice - buyPrice);
                totalPrice += sellPrice;
            }
            int finalTotalQty = totalQty;
            int finalTotalPrice = totalPrice;
            int finalTotalProfit = totalProfit;
            runOnUiThread(() -> {
                contentQtyTotal.setText(String.valueOf(finalTotalQty));
                contentTotal.setText(String.valueOf(finalTotalPrice));
                contentProfit.setText(String.valueOf(finalTotalProfit));
            });
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

    }

    private void retrieveProducts() {
        new Thread(() -> {
            ProductsDao productsDao = appDatabase.productsDao();
            List<Products> productsList = productsDao.getAllProducts();
            List<Products> activeProduct = new ArrayList<>();
            for (Products entity : productsList) {
                if (entity.getDeletedAt() == null) {
                    activeProduct.add(entity);
                }
            }

            // Update the noteList and notify the adapter
            cartList.clear();
            cartList.addAll(activeProduct);

            runOnUiThread(() ->
                    cartAdapter.setFilter(cartList)
            );
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveProducts();
    }
}