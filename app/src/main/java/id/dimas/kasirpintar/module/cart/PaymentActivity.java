package id.dimas.kasirpintar.module.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.component.FailedDialog;
import id.dimas.kasirpintar.component.SuccessDialog;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.Users;
import id.dimas.kasirpintar.module.menu.HomeActivity;

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

        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = dateFormat.format(currentDate);

        Intent intentExtra = getIntent();

        List<OrdersDetail> receivedOrdersDetails = (List<OrdersDetail>) intentExtra.getSerializableExtra("ordersDetails");
        Orders receivedOrders = (Orders) intentExtra.getSerializableExtra("orders");

        if (receivedOrders != null) {
            contentTotal.setText(String.valueOf(receivedOrders.amount));
            contentTrxDate.setText(formattedDate);
        }

        cvDPay.setOnClickListener(v -> {
            if (etTotalBayar.getText().toString().isEmpty()) {
                etTotalBayar.setError("Total bayar tidak boleh kosong");
                return;
            }

            receivedOrders.customerId = etCustomerName.getText().toString();

            int payAmount = Integer.parseInt(etTotalBayar.getText().toString());

            if (payAmount >= receivedOrders.getAmount()) {
                receivedOrders.setDifferent(payAmount - receivedOrders.getAmount());
                receivedOrders.setOrderStatus(MyApp.Status.COMPLETED.name());
            } else {
                receivedOrders.setDifferent(receivedOrders.getAmount() - payAmount);
                receivedOrders.setOrderStatus(MyApp.Status.PROCESSING.name());
            }
            String paidDate = dateFormat.format(currentDate);
            receivedOrders.setPaidAt(paidDate);
            receivedOrders.setPayAmount(payAmount);

            new Thread(() -> {

                long id = appDatabase.ordersDao().upsertOrders(receivedOrders);
                receivedOrders.setId((int) id);
                if (id > 0) {
                    long idDetail = 0;
                    for (OrdersDetail ordersDetail : receivedOrdersDetails
                    ) {


                        ordersDetail.ordersId = (int) id;
                        idDetail = appDatabase.ordersDetailDao().upsertOrdersDetail(ordersDetail);
                        ordersDetail.setId((int) idDetail);
                    }

                    if (idDetail > 0) {
                        runOnUiThread(() -> {
                            SuccessDialog successDialog = new SuccessDialog(mContext, "Transaksi Berhasil", String.format("Total : %s, Profit : %s", receivedOrders.getAmount(), receivedOrders.getProfit()), () -> {
                                Intent intent = new Intent(this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            });

                            successDialog.show();

                        });
                    }

                    runOnUiThread(() -> {
                        MyApp.getPrintHelper().printBluetooth(mContext, receivedOrders, receivedOrdersDetails);
                    });

                } else {
                    runOnUiThread(() -> {
                        FailedDialog failedDialog = new FailedDialog(mContext, "Transaksi Gagal", "Gagal menyimpan transaksi");
                        failedDialog.show();
                    });
                }
            }).start();
        });
    }
}