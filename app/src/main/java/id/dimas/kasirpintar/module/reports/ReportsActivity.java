package id.dimas.kasirpintar.module.reports;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.ExcelExporter;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Buy;
import id.dimas.kasirpintar.model.Categories;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.ReportTrxItem;
import id.dimas.kasirpintar.model.ReportTrxItemStock;

public class ReportsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private CardView cvRTrx;
    private CardView cvRSellProduct;
    private CardView cvRStockProduct;
    private CardView cvRCategory;
    private CardView cvRBuy;
    private AppDatabase appDatabase;
    private Context mContext;
    private SharedPreferenceHelper sharedPreferenceHelper;

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
        cvRBuy = (CardView) findViewById(R.id.cvRBuy);

        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Laporan");
        tvRightTitle.setVisibility(View.GONE);
        mContext = this;
        appDatabase = MyApp.getAppDatabase();
        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);

        cvRTrx.setOnClickListener(v -> {
            DatePickerdialog(1);
        });
        cvRSellProduct.setOnClickListener(v -> {
            DatePickerdialog(2);
        });
        cvRStockProduct.setOnClickListener(v -> {
//            DatePickerdialog(3);
            getTrxItemStock();
        });
        cvRCategory.setOnClickListener(v -> {
//            DatePickerdialog(4);
            getCategoryReport();
        });
        cvRBuy.setOnClickListener(v -> {
            DatePickerdialog(5);
        });

    }

    private void DatePickerdialog(int i) {
        // Creating a MaterialDatePicker builder for selecting a date range
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");

        // Building the date picker dialog
        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(selection -> {

            // Retrieving the selected start and end dates
            Long startDate = selection.first;
            Long endDate = selection.second;

            // Formating the selected dates as strings
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String startDateString = sdf.format(new Date(startDate));
            String endDateString = sdf.format(new Date(endDate));

            // Creating the date range string
            String selectedDateRange = startDateString + "-" + endDateString;

            // Displaying the selected date range in the TextView
            Log.d("DatePickerdialog", String.format("type : %s, selectedDateRange : %s", String.valueOf(i), selectedDateRange));

            switch (i) {
                case 1:
                    getTrxByDate(selectedDateRange);
                    break;
                case 2:
                    getTrxItem(selectedDateRange);
                    break;
                case 5:
                    getBuyReport(selectedDateRange);
                    break;

            }
        });

        // Showing the date picker dialog
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void getBuyReport(String selectedDateRange) {
        new Thread(() -> {
            List<Buy> reportTrxItems = appDatabase.buyDao().getAllBuyFilter(selectedDateRange.split("-")[0], selectedDateRange.split("-")[1], sharedPreferenceHelper.getShopId());
            runOnUiThread(() -> {
                ExcelExporter.exportToExcelBuy(mContext, reportTrxItems, String.format("Laporan_Pengeluaran_%s%s", selectedDateRange, sharedPreferenceHelper.getShopName()));
            });
        }).start();
    }

    private void getCategoryReport() {
        new Thread(() -> {
            List<Categories> categoriesList = appDatabase.categoriesDao().getAllCategoriesById(sharedPreferenceHelper.getShopId());
            runOnUiThread(() -> {
                ExcelExporter.exportToExcelCategories(mContext, categoriesList, String.format("Laporan_Category_%s", sharedPreferenceHelper.getShopName()));
            });
        }).start();
    }

    private void getTrxItemStock() {
        new Thread(() -> {
            List<ReportTrxItemStock> reportTrxItemStockList = appDatabase.productsDao().getAllProductsStockById(sharedPreferenceHelper.getShopId());
            runOnUiThread(() -> {
                ExcelExporter.exportToExcelItemStock(mContext, reportTrxItemStockList, String.format("Laporan_Item_Stock_%s", sharedPreferenceHelper.getShopName()));
            });
        }).start();
    }

    private void getTrxItem(String selectedDateRange) {
        new Thread(() -> {
            List<ReportTrxItem> reportTrxItems = appDatabase.ordersDetailDao().getAllItemTrx(selectedDateRange.split("-")[0], selectedDateRange.split("-")[1], sharedPreferenceHelper.getShopId());
            runOnUiThread(() -> {
                ExcelExporter.exportToExcelItemTrx(mContext, reportTrxItems, String.format("Laporan_Item_Quantity_%s%s", selectedDateRange, sharedPreferenceHelper.getShopName()));

            });
        }).start();
    }

    private void getTrxByDate(String selectedDateRange) {
        new Thread(() -> {
            List<Orders> ordersList = appDatabase.ordersDao().getAllOrdersById(sharedPreferenceHelper.getShopId());
            if (ordersList != null) {
                for (int i = 0; i < ordersList.size(); i++) {
                    List<OrdersDetail> ordersDetailList = appDatabase.ordersDetailDao().getAllOrdersDetailByDate(ordersList.get(i).getId(), selectedDateRange.split("-")[0], selectedDateRange.split("-")[1], sharedPreferenceHelper.getShopId());
                    ordersList.get(i).setOrdersDetailList(ordersDetailList);
                }
            }
            runOnUiThread(() -> {
                ExcelExporter.exportToExcelTrx(mContext, ordersList, String.format("Laporan_Transaksi_%s%s", selectedDateRange, sharedPreferenceHelper.getShopName()));

            });
        }).start();
    }


}