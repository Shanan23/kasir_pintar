package id.dimas.kasirpintar.module.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.helper.dao.ProductsDao;
import id.dimas.kasirpintar.model.Orders;
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

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;

    private List<OrdersDetail> ordersDetails;
    private Orders orders;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        toolbar = findViewById(R.id.toolbar);
        cvBack = findViewById(R.id.cvBack);
        tvLeftTitle = findViewById(R.id.tvLeftTitle);
        tvRightTitle = findViewById(R.id.tvRightTitle);

        contentQtyTotal = findViewById(R.id.contentQtyTotal);
        contentTotal = findViewById(R.id.contentTotal);
        contentProfit = findViewById(R.id.contentProfit);
        cvPay = findViewById(R.id.cvPay);

        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Transaksi");
        tvRightTitle.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.rvCart);
        appDatabase = MyApp.getAppDatabase();
        sharedPreferenceHelper = new SharedPreferenceHelper(this);

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList, ordersDetailList -> {
            int totalQty = 0;
            int totalPrice = 0;
            int totalProfit = 0;
            ordersDetails = ordersDetailList;
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

                orders = new Orders();
                orders.amount = finalTotalPrice;
                orders.different = finalTotalPrice;
                orders.profit = finalTotalProfit;
                orders.orderStatus = MyApp.Status.PENDING.name();
            });
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        cvPay.setOnClickListener(v -> {
            if (ordersDetails == null) {
                return;
            }
            if (ordersDetails.size() == 0) {
                return;
            }


            Date currentDate = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String formattedDate = dateFormat.format(currentDate);
            orders.setOrderDate(formattedDate);

            Intent intent = new Intent(this, PaymentActivity.class);

            intent.putExtra("ordersDetails", new ArrayList<>(ordersDetails)); // ArrayList implements Serializable
            intent.putExtra("orders", orders);

            startActivity(intent);
        });
    }

    private void retrieveProducts() {
        new Thread(() -> {
            ProductsDao productsDao = appDatabase.productsDao();
            List<Products> productsList = productsDao.getAllProducts(sharedPreferenceHelper.getShopId());
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