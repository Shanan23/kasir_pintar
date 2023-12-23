package id.dimas.kasirpintar.module.registration;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.component.FailedDialog;
import id.dimas.kasirpintar.component.SuccessDialog;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Users;
import id.dimas.kasirpintar.module.menu.HomeActivity;

public class VerificationActivity extends AppCompatActivity {

    private TextView titleVerification;
    private TextView messageVerification;
    private CardView cvRetry;
    private ImageView imgLbl;
    private CardView cvCheckVerification;
    private FirebaseAuth mAuth;
    private AppDatabase appDatabase;
    private FirebaseUser mUser;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        titleVerification = (TextView) findViewById(R.id.titleVerification);
        messageVerification = (TextView) findViewById(R.id.messageVerification);
        retryEmail = (TextView) findViewById(R.id.retryEmail);
        cvRetry = (CardView) findViewById(R.id.cvRetry);
        imgLbl = (ImageView) findViewById(R.id.imgLbl);
        cvCheckVerification = (CardView) findViewById(R.id.cvCheckVerification);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);

        tvLeftTitle.setText("Verifikasi Email");
        tvRightTitle.setVisibility(View.INVISIBLE);
        mContext = this;
        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);
        mAuth = FirebaseAuth.getInstance();
        appDatabase = MyApp.getAppDatabase();
        mUser = mAuth.getCurrentUser();
        retryEmail.setText(getString(R.string.retry_email, mUser.getEmail()));

        users = new Users();

        cvRetry.setOnClickListener(v -> {
            doSendEmail();
        });
        cvBack.setOnClickListener(v -> finish());

        cvCheckVerification.setOnClickListener(v -> {
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            if (mUser != null) {
                mUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Check the updated email verification status
                        boolean isEmailVerified = mUser.isEmailVerified();
                        Log.d(TAG, String.format("isEmailVerified:%s", isEmailVerified));

                        sharedPreferenceHelper.setLoggedIn(true);
                        sharedPreferenceHelper.setIsSavedPin(false);
                        sharedPreferenceHelper.saveUsername(mUser.getEmail());

                        new Thread(() -> {
                            Users users = appDatabase.usersDao().getUserById(mUser.getUid());
                            if (users == null) {
                                users = new Users();
                            }
                            users.setId(mUser.getUid());
                            users.setActive(true);
                            users.setEmail(mUser.getEmail());
                            users.setName(mUser.getDisplayName());
                            sharedPreferenceHelper.saveShopId(users.getOutletId());

                            appDatabase.usersDao().upsertUsers(users);
                        }).start();

                        if (isEmailVerified) {
                            Intent intent = new Intent(mContext, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }

        });
    }

    private void doSendEmail() {
        if (mUser != null) {
            mUser.sendEmailVerification().addOnCompleteListener(VerificationActivity.this, task1 -> {
                if (task1.isSuccessful()) {
                    Log.d(TAG, "sendEmailVerification:success");
                    users.id = mUser.getUid();
                    new UsersDbAsync(appDatabase, users, "getById", new UsersDbAsync.AsyncTaskListener() {
                        @Override
                        public void onTaskComplete(Users result) {
                            users = result;
                            users.id = mUser.getUid();
                            users.isVerificationSend = true;
                            new UsersDbAsync(appDatabase, users, "upsert", null).execute();

                            successDialog = new SuccessDialog(mContext, "Registrasi Berhasil", getString(R.string.check_email), () -> cvCheckVerification.performClick());
                            successDialog.show();
                        }

                        @Override
                        public void onTaskFailed() {

                        }
                    }).execute();
                } else {
                    Log.d(TAG, "sendEmailVerification:failure", task1.getException());

//                    users.isVerificationSend = false;
//                    new UsersDbAsync(appDatabase, users, "upsert", null).execute();


                    String exceptionMessage = task1.getException().getMessage();
                    if (exceptionMessage != null) {
                        if (exceptionMessage.contains("We have blocked all requests from this device due to unusual activity. Try again later.")) {
                            failedDialog = new FailedDialog(mContext, "Registrasi Gagal", "Terlalu banyak percobaan, coba lagi beberapa saat.");
                        } else {
                            failedDialog = new FailedDialog(mContext, "Registrasi Gagal", "Terjadi kesalahan");
                        }
                    } else {
                        failedDialog = new FailedDialog(mContext, "Registrasi Gagal", "Terjadi kesalahan");
                    }
                    failedDialog.show();
                }
            });
        }
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
