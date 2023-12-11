package id.dimas.kasirpintar.module.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.module.product.ProductActivity;

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

        ivProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductActivity.class);
            startActivity(intent);
        });
    }
}