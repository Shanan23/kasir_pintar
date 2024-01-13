package id.dimas.kasirpintar.module.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.OrdersDetail;

public class PaymentActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private TextView lblTotal;
    private TextView contentTotal;
    private CardView cvTotalPay;
    private TextInputEditText etCustomerName;
    private TextInputEditText etTotalBayar;
    private CardView cvCustomer;
    private TextView contentCustomerName;
    private CardView cvDate;
    private TextView contentTrxDate;
    private CardView cvDPay;
    private AppDatabase appDatabase;
    private Context mContext;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        lblTotal = (TextView) findViewById(R.id.lblTotal);
        contentTotal = (TextView) findViewById(R.id.contentTotal);
        cvTotalPay = (CardView) findViewById(R.id.cvTotalPay);
        etTotalBayar = (TextInputEditText) findViewById(R.id.etTotalBayar);
        cvCustomer = (CardView) findViewById(R.id.cvCustomer);
        cvDate = (CardView) findViewById(R.id.cvDate);
        contentTrxDate = (TextView) findViewById(R.id.contentTrxDate);
        cvDPay = (CardView) findViewById(R.id.cvDPay);
        etCustomerName = (TextInputEditText) findViewById(R.id.etCustomerName);

        mContext = this;
        appDatabase = MyApp.getAppDatabase();
        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);


        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Pembayaran");
        tvRightTitle.setVisibility(View.GONE);

        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = dateFormat.format(currentDate);

        Intent intentExtra = getIntent();

        List<OrdersDetail> receivedOrdersDetails = (List<OrdersDetail>) intentExtra.getSerializableExtra("ordersDetails");
        Orders receivedOrders = (Orders) intentExtra.getSerializableExtra("orders");

        if (receivedOrders != null) {
            contentTotal.setText(String.valueOf(receivedOrders.different));
            contentTrxDate.setText(formattedDate);

            if (receivedOrders.getCustomerId() != null) {
                if (!receivedOrders.getCustomerId().equalsIgnoreCase("")) {
                    etCustomerName.setText(receivedOrders.getCustomerId());
                    etCustomerName.setEnabled(false);
                }
            }
        }

        new Thread(() -> {
            receivedOrders.setOrderStatus(MyApp.Status.PENDING.name());
            receivedOrders.setIdOutlet(sharedPreferenceHelper.getShopId());
            long id = appDatabase.ordersDao().upsertOrders(receivedOrders);
            Log.d("Orders.id", String.valueOf(id));
            if (id > 0) {
                receivedOrders.setId((int) id);
            }

            long idDetail = 0;
            for (OrdersDetail ordersDetail : receivedOrdersDetails
            ) {
                ordersDetail.ordersId = receivedOrders.getId();
                ordersDetail.setIdOutlet(sharedPreferenceHelper.getShopId());
                idDetail = appDatabase.ordersDetailDao().upsertOrdersDetail(ordersDetail);
                ordersDetail.setId((int) idDetail);
            }
        }).start();

        cvDPay.setOnClickListener(v -> {
            if (etTotalBayar.getText().toString().isEmpty()) {
                etTotalBayar.setError("Total bayar tidak boleh kosong");
                return;
            }

            receivedOrders.customerId = etCustomerName.getText().toString();

            int payAmount = Integer.parseInt(etTotalBayar.getText().toString());

            if (payAmount >= receivedOrders.getDifferent()) {
                receivedOrders.setDifferent(payAmount - receivedOrders.getDifferent());
                receivedOrders.setOrderStatus(MyApp.Status.COMPLETED.name());
            } else {
                receivedOrders.setDifferent(receivedOrders.getDifferent() - payAmount);
                receivedOrders.setOrderStatus(MyApp.Status.PROCESSING.name());
            }

            String paidDate = dateFormat.format(currentDate);
            receivedOrders.setPaidAt(paidDate);
            receivedOrders.setPayAmount(payAmount);
            receivedOrders.setOrdersDetailList(receivedOrdersDetails);
            new Thread(() -> {
                receivedOrders.setIdOutlet(sharedPreferenceHelper.getShopId());
                appDatabase.ordersDao().upsertOrders(receivedOrders);
//                receivedOrders.setId((int) id);
//                if (id > 0) {
                long idDetail = 0;
                for (OrdersDetail ordersDetail : receivedOrdersDetails
                ) {
//                    ordersDetail.ordersId = (int) id;
                    ordersDetail.setIdOutlet(sharedPreferenceHelper.getShopId());
                    idDetail = appDatabase.ordersDetailDao().upsertOrdersDetail(ordersDetail);
                    ordersDetail.setId((int) idDetail);
                }

//                    if (idDetail > 0) {
                runOnUiThread(() -> {
//                            SuccessDialog successDialog = new SuccessDialog(mContext, "Transaksi Berhasil", String.format("Total : %s, Profit : %s", receivedOrders.getAmount(), receivedOrders.getProfit()), () -> {
                    Intent intent = new Intent(this, SuccessActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("total", receivedOrders.getAmount());
                    intent.putExtra("profit", receivedOrders.getProfit());
                    startActivity(intent);
//                            });

//                            successDialog.show();

                });
//                    }

                runOnUiThread(() -> {
                    MyApp.getPrintHelper().printBluetooth(mContext, receivedOrders, receivedOrdersDetails);
                });

//                } else {
//                    runOnUiThread(() -> {
//                        FailedDialog failedDialog = new FailedDialog(mContext, "Transaksi Gagal", "Gagal menyimpan transaksi");
//                        failedDialog.show();
//                    });
//                }
            }).start();
        });
    }
}