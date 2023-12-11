package id.dimas.kasirpintar.module.reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import id.dimas.kasirpintar.R;

public class ReportsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private CardView cvRTrx;
    private CardView cvRSellProduct;
    private CardView cvRStockProduct;
    private CardView cvRCategory;
    private CardView cvRClosing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        cvRTrx = (CardView) findViewById(R.id.cvRTrx);
        cvRSellProduct = (CardView) findViewById(R.id.cvRSellProduct);
        cvRStockProduct = (CardView) findViewById(R.id.cvRStockProduct);
        cvRCategory = (CardView) findViewById(R.id.cvRCategory);
        cvRClosing = (CardView) findViewById(R.id.cvRClosing);


        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Laporan");
        tvRightTitle.setVisibility(View.GONE);

        cvRTrx.setOnClickListener(v -> {
        });
        cvRSellProduct.setOnClickListener(v -> {
        });
        cvRStockProduct.setOnClickListener(v -> {
        });
        cvRCategory.setOnClickListener(v -> {
        });
        cvRClosing.setOnClickListener(v -> {
        });

    }
}