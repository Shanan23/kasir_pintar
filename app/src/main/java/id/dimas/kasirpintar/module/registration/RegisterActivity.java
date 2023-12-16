package id.dimas.kasirpintar.module.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.component.FailedDialog;
import id.dimas.kasirpintar.component.SuccessDialog;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.EmailHelper;
import id.dimas.kasirpintar.helper.HashUtils;
import id.dimas.kasirpintar.model.Outlets;
import id.dimas.kasirpintar.model.Users;
import id.dimas.kasirpintar.module.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    //    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private String pin;
    private String name;
    private final String TAG = "RegisterActivity";

    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private TextInputEditText etEmail;
    private AutoCompleteTextView actShopName;
    private TextInputEditText etName;
    private TextInputEditText etPassword;
    private TextInputEditText etPin;
    private CardView cvDaftar;
    private Context mContext;
    private CheckBox showPasswordCheckbox;
    private Toolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private FailedDialog failedDialog;
    private AppDatabase appDatabase;
    private Users users;
    ArrayAdapter<String> autoCompleteAdapter;
    private List<Outlets> outletsList;
    private boolean isNewOutlet;
    private int outletId;

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
        etPin = findViewById(R.id.etPin);
        actShopName = findViewById(R.id.actShopName);
        cvDaftar = findViewById(R.id.cvChangePassword);
        showPasswordCheckbox = findViewById(R.id.showPasswordCheckbox);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        mAuth = FirebaseAuth.getInstance();
        appDatabase = MyApp.getAppDatabase();

        cvBack.setVisibility(View.INVISIBLE);

        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show Password
                etPassword.setTransformationMethod(null);
                etPin.setTransformationMethod(null);
            } else {
                // Hide Password
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
                etPin.setTransformationMethod(new PasswordTransformationMethod());
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
            if (etPin.getText().toString().isEmpty()) {
                etPin.setError("Pin tidak boleh kosong");
                return;
            }

            email = etEmail.getText().toString();
            name = etName.getText().toString();
            password = etPassword.getText().toString();
            pin = HashUtils.hashPassword(etPin.getText().toString());
            doRegis();
        });

        setupAutoCompleteTextView();

    }

    private void getAllShopNames() {
        AtomicReference<List<String>> suggestions = new AtomicReference<>(new ArrayList<>());

        new Thread(() -> {
            outletsList = appDatabase.outletsDao().getAllOutlets();
            List<String> outletName = new ArrayList<>();
            for (Outlets outlet : outletsList) {
                outletName.add(outlet.name);
            }
            suggestions.set(outletName);
            // Set up AutoCompleteTextView with suggestions
            autoCompleteAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, suggestions.get());
            runOnUiThread(() -> actShopName.setAdapter(autoCompleteAdapter));
        }).start();
    }

    private void setupAutoCompleteTextView() {
        // Fetch existing shop names from the database
        getAllShopNames();

        actShopName.setAdapter(autoCompleteAdapter);

        actShopName.setOnItemClickListener((parent, view, position, id) -> {

        });

        actShopName.setOnDismissListener(() -> {
            String enteredText = actShopName.getText().toString().trim();
            if (enteredText.isEmpty()) {
                actShopName.setError("Nama Toko tidak boleh kosong");
                return;
            }
            int indexOutlet = getIndexByValue(enteredText);
            if (indexOutlet == -1) {
                isNewOutlet = true;

            } else {
                isNewOutlet = false;
                outletId = outletsList.get(indexOutlet).getId();
            }
        });
    }

    private int getIndexByValue(String searchValue) {
        for (int i = 0; i < outletsList.size(); i++) {
            if (outletsList.get(i).getName().equals(searchValue)) {
                return i;
            }
        }
        return -1;
    }

    private void doRegis() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        String generatedOtp = EmailHelper.generateOTP();
        Log.d("doRegis", "generatedOtp = " + generatedOtp);

        EmailHelper.sendSimpleMessage(email, name, generatedOtp);

        users = new Users();
        users.email = email;
        users.pin = pin;
        users.isActive = false;
        users.createdAt = new Date().toString();

        new Thread(() -> {
            if (isNewOutlet) {
                Outlets outlets = new Outlets();
                outlets.setName(actShopName.getText().toString());
                outlets.setAddress("");
                long newOutletId = appDatabase.outletsDao().upsertOutlets(outlets);
                users.setOutletId(String.valueOf(newOutletId));
                users.isAdmin = true;
            } else {
                users.setOutletId(String.valueOf(outletId));
            }

            long newUserId = appDatabase.usersDao().upsertUsers(users);
            users.setId(String.valueOf(newUserId));

            runOnUiThread(() -> {
                SuccessDialog successDialog = new SuccessDialog(mContext, "Registrasi Berhasil", getString(R.string.check_email), () -> {
                    Intent intent = new Intent(mContext, VerificationActivity.class);
                    intent.putExtra("otp", generatedOtp);
                    intent.putExtra("users", users);
                    startActivity(intent);
                });
                successDialog.show();
            });
        });
    }

//    public void doRegis() {
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Log.d(TAG, "createUserWithEmail:success");
//
//                    FirebaseUser user = mAuth.getCurrentUser();
//
//                    users = new Users();
//                    users.id = user.getUid();
//                    users.email = email;
//                    users.pin = pin;
//                    users.isActive = user.isEmailVerified();
//                    users.createdAt = new Date().toString();
//
//                    new UsersDbAsync(appDatabase, users).execute();
//
//                    if (user != null) {
//                        user.sendEmailVerification().addOnCompleteListener(RegisterActivity.this, task1 -> {
//                            if (task1.isSuccessful()) {
//                                Log.d(TAG, "sendEmailVerification:success");
//
//                                users.isVerificationSend = true;
//                                new UsersDbAsync(appDatabase, users).execute();
//
//                                doSetName(user);
//                                SuccessDialog successDialog = new SuccessDialog(mContext, "Registrasi Berhasil", getString(R.string.check_email), () -> {
//                                    Intent intent = new Intent(mContext, VerificationActivity.class);
//                                    startActivity(intent);
//                                });
//                                successDialog.show();
//                            } else {
//                                Log.d(TAG, "sendEmailVerification:failed");
//
//                                users.isVerificationSend = false;
//                                new UsersDbAsync(appDatabase, users).execute();
//
//                                Toast.makeText(mContext, "Failed to send verification email", Toast.LENGTH_SHORT).show();
//
//                                String exceptionMessage = task.getException().getMessage();
//                                if (exceptionMessage != null) {
//                                    if (exceptionMessage.contains("The email address is already in use by another account.")) {
//                                        failedDialog = new FailedDialog(mContext, "Registrasi Gagal", "Email sudah digunakan, silakan melakukan login");
//                                    } else {
//                                        failedDialog = new FailedDialog(mContext, "Registrasi Gagal", "Terjadi kesalahan");
//                                    }
//                                } else {
//                                    failedDialog = new FailedDialog(mContext, "Registrasi Gagal", "Terjadi kesalahan");
//                                }
//                                failedDialog.show();
//                            }
//                        });
//                    }
//                } else {
//                    Log.e(TAG, "createUserWithEmail:failure", task.getException());
//                    FailedDialog failedDialog = new FailedDialog(mContext, "Registrasi Gagal", task.getException().getMessage());
//                    failedDialog.show();
//                }
//            }
//        });
//    }
//
//    public void doSetName(FirebaseUser user) {
//        if (user != null) {
//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
//
//            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//
//                        users.name = user.getDisplayName();
//                        new UsersDbAsync(appDatabase, users).execute();
//
//                    } else {
//                        Exception exception = task.getException();
//                        Log.d(TAG, "sendEmailVerification:failed", exception);
//
//                    }
//                }
//            });
//        }
//    }

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
}