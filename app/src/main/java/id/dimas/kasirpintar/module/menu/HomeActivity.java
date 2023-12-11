package id.dimas.kasirpintar.module.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.HashUtils;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Users;
import id.dimas.kasirpintar.module.cart.CartActivity;
import id.dimas.kasirpintar.module.history.HistoryActivity;
import id.dimas.kasirpintar.module.product.ProductActivity;
import id.dimas.kasirpintar.module.reports.ReportsActivity;
import id.dimas.kasirpintar.module.security.SecurityActivity;
import id.dimas.kasirpintar.module.settings.SettingsActivity;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private ImageView imgHeader;
    private TextView lblHeader;
    private TextView lblSale;
    private TextView contentSale;
    private TextView lblProfit;
    private TextView contentProfit;
    private CardView cvProfile;
    private ImageView imgProfile;
    private TextView contentShopName;
    private TextView contentShopAddress;
    private ImageView imgEdit;
    private CardView ivProduct;
    private CardView ivHistory;
    private CardView ivClosing;
    private CardView ivReport;
    private CardView ivPrint;
    private CardView ivSetting;
    private CardView ivSecurity;
    private CardView cvTransaction;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private Context mContext;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        imgHeader = (ImageView) findViewById(R.id.imgHeader);
        lblHeader = (TextView) findViewById(R.id.lblHeader);
        lblSale = (TextView) findViewById(R.id.lblSale);
        contentSale = (TextView) findViewById(R.id.contentSale);
        lblProfit = (TextView) findViewById(R.id.lblProfit);
        contentProfit = (TextView) findViewById(R.id.contentProfit);
        cvProfile = (CardView) findViewById(R.id.cvProfile);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        contentShopName = (TextView) findViewById(R.id.contentShopName);
        contentShopAddress = (TextView) findViewById(R.id.contentShopAddress);
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        ivProduct = (CardView) findViewById(R.id.ivProduct);
        ivHistory = (CardView) findViewById(R.id.ivHistory);
        ivClosing = (CardView) findViewById(R.id.ivClosing);
        ivReport = (CardView) findViewById(R.id.ivReport);
        ivPrint = (CardView) findViewById(R.id.ivPrint);
        ivSetting = (CardView) findViewById(R.id.ivSetting);
        ivSecurity = (CardView) findViewById(R.id.ivSecurity);
        cvTransaction = (CardView) findViewById(R.id.cvTransaction);

        mContext = this;
        appDatabase = MyApp.getAppDatabase();

        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);
        if (!sharedPreferenceHelper.isSavedPin()) {
            showDialogPin();
        }

        ivProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductActivity.class);
            startActivity(intent);
        });

        ivHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });

        ivSecurity.setOnClickListener(v -> {
            Intent intent = new Intent(this, SecurityActivity.class);
            startActivity(intent);
        });

        ivReport.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportsActivity.class);
            startActivity(intent);
        });

        ivSetting.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        cvTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }

    void showDialogPin() {
        PinDialog.showPinDialog(mContext, pin -> {
            String hashPin = HashUtils.hashPassword(pin);
            Log.d("hashPin", hashPin);
            new Thread(() -> {
                Users users = appDatabase.usersDao().getUserByPin(sharedPreferenceHelper.getUsername(), hashPin);
                if (users != null) {
                    sharedPreferenceHelper.setIsSavedPin(true);
                } else {
                    runOnUiThread(() -> {
                        showDialogPin();
                    });
                }
            }).start();
        });
    }
}