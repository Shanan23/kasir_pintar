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

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Outlets;
import id.dimas.kasirpintar.model.Users;
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
    private AppDatabase appDatabase;
    private Outlets outlet;
    private CardView cvUser;

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
        cvUser = (CardView) findViewById(R.id.cvUser);

        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Pengaturan");
        tvRightTitle.setVisibility(View.GONE);

        mContext = this;
        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);
        appDatabase = MyApp.getAppDatabase();
        new Thread(() -> {
            outlet = appDatabase.outletsDao().getAllOutletsById(sharedPreferenceHelper.getShopId());
            runOnUiThread(() -> {
                if(outlet!=null) {
                    etShopName.setText(outlet.getName());
                    etAddress.setText(outlet.getAddress());
                }else {
                    etShopName.setText("");
                    etAddress.setText("");
                }
            });
        }).start();

        etEmail.setText(sharedPreferenceHelper.getUsername());
        swProfit.setChecked(sharedPreferenceHelper.isShowProfit());
        isProfitChecked = sharedPreferenceHelper.isShowProfit();
        swProfit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isProfitChecked = isChecked;
        });

        cvUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, UsersActivity.class);
            startActivity(intent);
        });

        ivSaveSetting.setOnClickListener(v -> {
            if (etShopName.getText().toString().isEmpty()) {
                etShopName.setError("Nama toko tidak boleh kosong");
                return;
            }
            if (etAddress.getText().toString().isEmpty()) {
                etAddress.setError("Alamat toko tidak boleh kosong");
                return;
            }
            if (outlet == null) {
                outlet = new Outlets();
            }

            outlet.setName(etShopName.getText().toString());
            outlet.setAddress(etAddress.getText().toString());

            new Thread(() -> {
                long idOutlet = appDatabase.outletsDao().upsertOutlets(outlet);
                sharedPreferenceHelper.saveShopId(String.valueOf(idOutlet));
                Users users = appDatabase.usersDao().getUserByEmail(sharedPreferenceHelper.getUsername());
                if (users == null) {
                    users = new Users();
                    users.setAdmin(true);
                }
                users.setOutletId(String.valueOf(idOutlet));
                users.isAdmin = true;
                appDatabase.usersDao().upsertUsers(users);
            }).start();

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