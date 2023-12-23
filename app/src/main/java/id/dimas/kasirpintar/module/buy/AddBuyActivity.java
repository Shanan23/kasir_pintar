package id.dimas.kasirpintar.module.buy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Buy;

public class AddBuyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private TextInputLayout tilBuyName;
    private TextInputEditText etBuyName;
    private TextInputLayout tilDesc;
    private TextInputEditText etDesc;
    private TextInputLayout tilAmount;
    private TextInputEditText etAmount;
    private CardView ivAddBuy;
    private AppDatabase appDatabase;
    private Buy buy;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        tilBuyName = (TextInputLayout) findViewById(R.id.tilBuyName);
        etBuyName = (TextInputEditText) findViewById(R.id.etBuyName);
        tilDesc = (TextInputLayout) findViewById(R.id.tilDesc);
        etDesc = (TextInputEditText) findViewById(R.id.etDesc);
        tilAmount = (TextInputLayout) findViewById(R.id.tilAmount);
        etAmount = (TextInputEditText) findViewById(R.id.etAmount);
        ivAddBuy = (CardView) findViewById(R.id.ivSaveSetting);


        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Pengeluaran");
        tvRightTitle.setVisibility(View.GONE);


        appDatabase = MyApp.getAppDatabase();
        sharedPreferenceHelper = new SharedPreferenceHelper(this);

        buy = new Buy();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_EXTRA")) {
            buy = (Buy) intent.getSerializableExtra("PRODUCT_EXTRA");
            etBuyName.setText(buy != null ? buy.getName() : "");
            etDesc.setText(buy != null ? buy.getDesc() : "");
            etAmount.setText(buy != null ? String.valueOf(buy.getAmount()) : "");
        }

        ivAddBuy.setOnClickListener(v -> {
            int stock = 0;
            if (etBuyName.getText().toString().isEmpty()) {
                etBuyName.setError("Nama tidak boleh kosong");
                return;
            }
            if (etDesc.getText().toString().isEmpty()) {
                etDesc.setError("Deskripsi tidak boleh kosong");
                return;
            }
            if (etAmount.getText().toString().isEmpty()) {
                etAmount.setError("Amount tidak boleh kosong");
                return;
            }

            buy.setName(etBuyName.getText().toString());
            buy.setDesc(etDesc.getText().toString());
            buy.setAmount(Integer.parseInt(etAmount.getText().toString()));

            Date currentDate = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String formattedDate = dateFormat.format(currentDate);

            buy.setCreatedAt(formattedDate);

            new Thread(() -> {
                buy.setIdOutlet(sharedPreferenceHelper.getShopId());
                long upsertBuy = appDatabase.buyDao().upsertBuy(buy);
                if (upsertBuy > 0) {
                    Log.d("upsertBuy", "berhasil upsertBuy");

                } else {
                    Log.e("upsertBuy", "gagal upsertBuy");
                }
                finish();
            }).start();
        });
    }
}