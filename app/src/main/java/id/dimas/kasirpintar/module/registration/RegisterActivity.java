package id.dimas.kasirpintar.module.registration;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Date;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.component.FailedDialog;
import id.dimas.kasirpintar.component.SuccessDialog;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.model.Users;
import id.dimas.kasirpintar.module.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private String name;
    private final String TAG = "RegisterActivity";

    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private TextInputEditText etEmail;
    private TextInputEditText etName;
    private TextInputEditText etPassword;
    private CardView cvDaftar;
    private Context mContext;
    private CheckBox showPasswordCheckbox;
    private Toolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private FailedDialog failedDialog;
    private AppDatabase appDatabase;
    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);
        cvBack = findViewById(R.id.cvBack);
        tvLeftTitle = findViewById(R.id.tvLeftTitle);
        tvRightTitle = findViewById(R.id.tvRightTitle);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        cvDaftar = findViewById(R.id.cvDaftar);
        showPasswordCheckbox = findViewById(R.id.showPasswordCheckbox);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        appDatabase = MyApp.getAppDatabase();

        cvBack.setVisibility(View.INVISIBLE);

        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show Password
                etPassword.setTransformationMethod(null);
            } else {
                // Hide Password
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });


        mContext = this;
        tvLeftTitle.setText("Daftar");
        tvRightTitle.setText("Masuk");
        tvRightTitle.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        cvDaftar.setOnClickListener(v -> {
            if (etEmail.getText().toString().isEmpty()) {
                etEmail.setError("Email tidak boleh kosong");
                return;
            }
            if (etName.getText().toString().isEmpty()) {
                etName.setError("Nama tidak boleh kosong");
                return;
            }
            if (etPassword.getText().toString().isEmpty()) {
                etPassword.setError("Password tidak boleh kosong");
                return;
            }

            email = etEmail.getText().toString();
            name = etName.getText().toString();
            password = etPassword.getText().toString();
            doRegis();
        });
    }

    public void doRegis() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");

                    FirebaseUser user = mAuth.getCurrentUser();

                    users = new Users();
                    users.id = user.getUid();
                    users.email = email;
                    users.isActive = user.isEmailVerified();
                    users.createdAt = new Date().toString();

                    new UsersDbAsync(appDatabase, users).execute();

                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(RegisterActivity.this, task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d(TAG, "sendEmailVerification:success");

                                users.isVerificationSend = true;
                                new UsersDbAsync(appDatabase, users).execute();

                                doSetName(user);
                                SuccessDialog successDialog = new SuccessDialog(mContext, "Registrasi Berhasil", getString(R.string.check_email), () -> {
                                    Intent intent = new Intent(mContext, VerificationActivity.class);
                                    startActivity(intent);
                                });
                                successDialog.show();
                            } else {
                                Log.d(TAG, "sendEmailVerification:failed");

                                users.isVerificationSend = false;
                                new UsersDbAsync(appDatabase, users).execute();

                                Toast.makeText(mContext, "Failed to send verification email", Toast.LENGTH_SHORT).show();

                                String exceptionMessage = task.getException().getMessage();
                                if (exceptionMessage != null) {
                                    if (exceptionMessage.contains("The email address is already in use by another account.")) {
                                        failedDialog = new FailedDialog(mContext, "Registrasi Gagal", "Email sudah digunakan, silakan melakukan login");
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
                } else {
                    Log.e(TAG, "createUserWithEmail:failure", task.getException());
                    FailedDialog failedDialog = new FailedDialog(mContext, "Registrasi Gagal", task.getException().getMessage());
                    failedDialog.show();
                }
            }
        });
    }

    public void doSetName(FirebaseUser user) {
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        users.name = user.getDisplayName();
                        new UsersDbAsync(appDatabase, users).execute();

                    } else {
                        Exception exception = task.getException();
                        Log.d(TAG, "sendEmailVerification:failed", exception);

                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.double_back), Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    private static class UsersDbAsync extends AsyncTask<Void, Void, Void> {

        private AppDatabase appDatabase;
        private Users users;

        public UsersDbAsync(AppDatabase appDatabase, Users users) {
            this.appDatabase = appDatabase;
            this.users = users;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDatabase.usersDao().upsertUsers(users);
            return null;
        }
    }
}