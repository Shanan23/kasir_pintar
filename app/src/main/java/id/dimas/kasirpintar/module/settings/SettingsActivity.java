package id.dimas.kasirpintar.module.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.module.menu.HomeActivity;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private TextInputLayout tilShopName;
    private TextInputEditText etShopName;
    private TextInputLayout tilAddress;
    private TextInputEditText etAddress;
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private SwitchCompat swProfit;
    private CardView ivSaveSetting;
    private Context mContext;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private boolean isProfitChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        tilShopName = (TextInputLayout) findViewById(R.id.tilShopName);
        etShopName = (TextInputEditText) findViewById(R.id.etShopName);
        tilAddress = (TextInputLayout) findViewById(R.id.tilAddress);
        etAddress = (TextInputEditText) findViewById(R.id.etAddress);
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        etEmail = (TextInputEditText) findViewById(R.id.etEmail);
        swProfit = (SwitchCompat) findViewById(R.id.swProfit);
        ivSaveSetting = (CardView) findViewById(R.id.ivSaveSetting);

        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Pengaturan");
        tvRightTitle.setVisibility(View.GONE);

        mContext = this;
        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);

        etShopName.setText(sharedPreferenceHelper.getShopName());
        etAddress.setText(sharedPreferenceHelper.getShopAddress());
        etEmail.setText(sharedPreferenceHelper.getUsername());
        swProfit.setChecked(sharedPreferenceHelper.isShowProfit());
        isProfitChecked = sharedPreferenceHelper.isShowProfit();
        swProfit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isProfitChecked = isChecked;
        });

        ivSaveSetting.setOnClickListener(v -> {
            sharedPreferenceHelper.saveShopName(etShopName.getText().toString());
            sharedPreferenceHelper.saveShopAddress(etAddress.getText().toString());
            sharedPreferenceHelper.setShowProfit(isProfitChecked);

            Intent intent = new Intent(mContext, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}