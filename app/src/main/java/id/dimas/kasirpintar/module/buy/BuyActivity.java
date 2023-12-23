package id.dimas.kasirpintar.module.buy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import id.dimas.kasirpintar.model.Buy;

public class BuyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private LinearLayout searchLayout;
    private ImageView searchIcon;
    private EditText editTextSearch;
    private ImageButton clearButton;
    private RecyclerView recyclerViewBuy;
    private CardView ivAddBuy;
    private AppDatabase appDatabase;
    List<Buy> buyList;
    BuyAdapter buyAdapter;
    private Context mContext;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        searchIcon = (ImageView) findViewById(R.id.searchIcon);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        clearButton = (ImageButton) findViewById(R.id.clearButton);
        recyclerViewBuy = (RecyclerView) findViewById(R.id.recyclerViewBuy);
        ivAddBuy = (CardView) findViewById(R.id.ivAddCategory);

        mContext = this;
        appDatabase = MyApp.getAppDatabase();
        sharedPreferenceHelper = new SharedPreferenceHelper(this);

        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Pengeluaran");
        tvRightTitle.setVisibility(View.GONE);

        buyList = new ArrayList<>();
        buyAdapter = new BuyAdapter(mContext, buyList, new BuyAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Buy buy) {

            }

            @Override
            public void onDeleteClick(Buy buy) {
                Date currentDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(currentDate);

                buy.setDeletedAt(formattedDate);
buy.setIdOutlet(sharedPreferenceHelper.getShopId());
                new Thread(() -> appDatabase.buyDao().upsertBuy(buy)).start();
            }
        });


        recyclerViewBuy.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewBuy.setAdapter(buyAdapter);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().trim();
                filterProducts(searchText);
                updateClearButtonVisibility();
            }
        });

        ivAddBuy.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AddBuyActivity.class);
            startActivity(intent);
        });
    }

    private void updateClearButtonVisibility() {
        clearButton.setVisibility(editTextSearch.getText().length() > 0 ? View.VISIBLE : View.GONE);
    }

    private void filterProducts(String query) {
        // Perform filtering logic based on the query
        List<Buy> filteredList = new ArrayList<>();

        for (Buy buy : buyList) {
            if (buy.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(buy);
            }
        }

        buyAdapter.setFilter(filteredList);
    }

    private void retrieveCategories() {
        new Thread(() -> {
            List<Buy> allBuy = appDatabase.buyDao().getAllBuy();
            List<Buy> activeBuy = new ArrayList<>();
            for (Buy entity : allBuy) {
                if (entity.getDeletedAt() == null) {
                    activeBuy.add(entity);
                }
            }

            // Update the noteList and notify the adapter
            buyList.clear();
            buyList.addAll(activeBuy);

            runOnUiThread(() ->
                    buyAdapter.setFilter(buyList)
            );
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveCategories();

    }
}