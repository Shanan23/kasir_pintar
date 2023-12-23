package id.dimas.kasirpintar.module.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.HashUtils;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.Outlets;
import id.dimas.kasirpintar.model.Products;
import id.dimas.kasirpintar.model.Profit;
import id.dimas.kasirpintar.model.Users;
import id.dimas.kasirpintar.module.buy.BuyActivity;
import id.dimas.kasirpintar.module.cart.CartActivity;
import id.dimas.kasirpintar.module.history.HistoryActivity;
import id.dimas.kasirpintar.module.product.ProductActivity;
import id.dimas.kasirpintar.module.reports.ReportsActivity;
import id.dimas.kasirpintar.module.security.SecurityActivity;
import id.dimas.kasirpintar.module.settings.SettingsActivity;
import id.dimas.kasirpintar.module.splash.SplashActivity;

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
    private CardView ivBuy;
    private CardView ivReport;
    private CardView ivPrint;
    private CardView ivSetting;
    private CardView ivSecurity;
    private CardView cvTransaction;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private Context mContext;
    private AppDatabase appDatabase;
    private ConstraintLayout profitHome;
    private Outlets outlet;

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
        ivBuy = (CardView) findViewById(R.id.ivBuy);
        ivReport = (CardView) findViewById(R.id.ivReport);
        ivPrint = (CardView) findViewById(R.id.ivPrint);
        ivSetting = (CardView) findViewById(R.id.ivSetting);
        ivSecurity = (CardView) findViewById(R.id.ivSecurity);
        cvTransaction = (CardView) findViewById(R.id.cvTransaction);
        profitHome = (ConstraintLayout) findViewById(R.id.profitHome);

        mContext = this;
        appDatabase = MyApp.getAppDatabase();
        cvBack.setVisibility(View.INVISIBLE);
        tvLeftTitle.setText("KASIR PINTAR");
        tvRightTitle.setText("Logout");
        tvRightTitle.setOnClickListener(v -> {
            sharedPreferenceHelper.clearAll();
            Intent intent = new Intent(mContext, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);


        new Thread(() -> {
            outlet = appDatabase.outletsDao().getAllOutletsById(sharedPreferenceHelper.getShopId());
            runOnUiThread(() -> {
                if (outlet != null) {
                    contentShopName.setText(outlet.getName());
                    contentShopAddress.setText(outlet.getAddress());
                } else {
                    contentShopName.setText("");
                    contentShopAddress.setText("");
                }
            });
        }).start();


        if (!sharedPreferenceHelper.isSavedPin()) {
            showDialogPin();
        }

        if (MyApp.getPrintHelper().selectedDevice == null) {
            MyApp.getPrintHelper().browseBluetoothDevice(mContext);
        }

        ivProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductActivity.class);
            startActivity(intent);
        });

        ivBuy.setOnClickListener(v -> {
            Intent intent = new Intent(this, BuyActivity.class);
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

        imgEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        cvTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        ivPrint.setOnClickListener(v -> {
            new Thread(() -> {
                Orders lastOrder = appDatabase.ordersDao().getLastOrders();
                List<OrdersDetail> ordersDetailList = appDatabase.ordersDetailDao().getAllOrdersDetailById(lastOrder.getId());
                for (int i = 0; i < ordersDetailList.size(); i++) {
                    Products products = appDatabase.productsDao().getAllProductsById(ordersDetailList.get(i).getItemId());
                    ordersDetailList.get(i).setProducts(products);
                }
                lastOrder.setOrdersDetailList(ordersDetailList);
                MyApp.getPrintHelper().printBluetooth(mContext, lastOrder, ordersDetailList);
            }).start();
        });
    }

    void getProfit() {
        new Thread(() -> {
            Profit lastOrder = appDatabase.ordersDao().getAllProfit();
            runOnUiThread(() -> {
                contentSale.setText(String.valueOf(lastOrder.total));
                contentProfit.setText(String.valueOf(lastOrder.profit));
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfit();
        if (sharedPreferenceHelper.isShowProfit()) {
            profitHome.setVisibility(View.VISIBLE);
        } else {
            profitHome.setVisibility(View.GONE);
        }


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