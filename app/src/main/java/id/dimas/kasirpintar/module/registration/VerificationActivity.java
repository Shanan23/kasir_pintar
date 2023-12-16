package id.dimas.kasirpintar.module.registration;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.component.FailedDialog;
import id.dimas.kasirpintar.component.SuccessDialog;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.EmailHelper;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Users;
import id.dimas.kasirpintar.module.menu.HomeActivity;

public class VerificationActivity extends AppCompatActivity {

    private TextView titleVerification;
    private TextView messageVerification;
    private CardView cvRetry;
    private ImageView imgLbl;
    private CardView cvCheckVerification;
    //    private FirebaseAuth mAuth;
    private AppDatabase appDatabase;
    //    private FirebaseUser mUser;
    private String TAG = "VerificationActivity";
    private Users users;
    private Context mContext;
    private FailedDialog failedDialog;
    private TextView retryEmail;
    private SuccessDialog successDialog;
    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    SharedPreferenceHelper sharedPreferenceHelper;
    private TextInputEditText etOtp;
    private Users receivedUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        titleVerification = (TextView) findViewById(R.id.titleVerification);
        messageVerification = (TextView) findViewById(R.id.messageVerification);
        retryEmail = (TextView) findViewById(R.id.retryEmail);
        cvRetry = (CardView) findViewById(R.id.cvRetry);
        imgLbl = (ImageView) findViewById(R.id.imgLbl);
        etOtp = (TextInputEditText) findViewById(R.id.etOtp);
        cvCheckVerification = (CardView) findViewById(R.id.cvCheckVerification);

        Intent intentExtra = getIntent();
        String receivedOtp = intentExtra.getStringExtra("otp");
        receivedUser = (Users) intentExtra.getSerializableExtra("users");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);

        tvLeftTitle.setText("Verifikasi Email");
        tvRightTitle.setVisibility(View.INVISIBLE);
        mContext = this;
        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);
        appDatabase = MyApp.getAppDatabase();
        retryEmail.setText(getString(R.string.retry_email, receivedUser.getEmail()));


        cvRetry.setOnClickListener(v -> {
            doSendEmail();
        });
        cvBack.setOnClickListener(v -> finish());

        cvCheckVerification.setOnClickListener(v -> {
            if (receivedOtp.equalsIgnoreCase(etOtp.getText().toString())) {
                boolean isEmailVerified = true;
                Log.d(TAG, String.format("isEmailVerified:%s", isEmailVerified));

                sharedPreferenceHelper.setLoggedIn(true);
                sharedPreferenceHelper.setIsSavedPin(false);
                sharedPreferenceHelper.saveUsername(receivedUser.getEmail());

                new Thread(() -> {
                    Users users = appDatabase.usersDao().getUserById(receivedUser.getId());
                    users.setActive(true);
                    appDatabase.usersDao().upsertUsers(users);
                }).start();

                if (isEmailVerified) {
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else {
                failedDialog = new FailedDialog(mContext, "Registrasi Gagal", "Otp tidak sesuai");
                failedDialog.show();
            }
        });
    }

    private void doSendEmail() {

        String generatedOtp = EmailHelper.generateOTP();
        Log.d("doSendEmail", "generatedOtp = " + generatedOtp);

        EmailHelper.sendEmail(receivedUser.getEmail(), receivedUser.getName(), generatedOtp);
    }

    private static class UsersDbAsync extends AsyncTask<Void, Void, Users> {

        private final AsyncTaskListener asyncTaskListener;
        private AppDatabase appDatabase;
        private final Users users;
        private final String type;

        public UsersDbAsync(AppDatabase appDatabase, Users users, String type, AsyncTaskListener asyncTaskListener) {
            this.appDatabase = appDatabase;
            this.users = users;
            this.type = type;
            this.asyncTaskListener = asyncTaskListener;
        }

        @Override
        protected Users doInBackground(Void... voids) {
            if (type.equalsIgnoreCase("upsert")) {
                appDatabase.usersDao().upsertUsers(users);
            } else if (type.equalsIgnoreCase("getById")) {
                return appDatabase.usersDao().getUserById(users.id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Users result) {
            super.onPostExecute(result);
            if (result != null) {
                if (asyncTaskListener != null) {
                    asyncTaskListener.onTaskComplete(result);
                }
            } else {
                if (asyncTaskListener != null) {
                    asyncTaskListener.onTaskFailed();
                }
            }
        }

        public interface AsyncTaskListener {
            void onTaskComplete(Users result);

            void onTaskFailed();
        }
    }
}
