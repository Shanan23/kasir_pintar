package id.dimas.kasirpintar.module.cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.module.menu.HomeActivity;

public class SuccessActivity extends AppCompatActivity {

    private ImageView successIcon;
    private TextView pinTitle;
    private TextView successMessage;
    private CardView cvOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);


        successIcon = (ImageView) findViewById(R.id.successIcon);
        pinTitle = (TextView) findViewById(R.id.pinTitle);
        successMessage = (TextView) findViewById(R.id.successMessage);
        cvOk = (CardView) findViewById(R.id.cvOk);


        Intent intentExtra = getIntent();

        int profit = intentExtra.getIntExtra("profit", 0);
        int total = intentExtra.getIntExtra("total", 0);

        pinTitle.setText("TRANSAKSI BERHASIL DISIMPAN");
        successMessage.setText(String.format("Total : %s\nProfit : %s", total, profit));

        cvOk.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}