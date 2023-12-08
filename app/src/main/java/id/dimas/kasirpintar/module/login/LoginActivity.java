package id.dimas.kasirpintar.module.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.component.FailedDialog;
import id.dimas.kasirpintar.component.SuccessDialog;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.module.menu.MenuActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "LoginActivity";
    private String email;
    private String password;


    private Toolbar toolbar;
    private CardView cvBack;
    private CardView cvLogin;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etPassword;
    private CheckBox showPasswordCheckbox;
    private FailedDialog failedDialog;
    private Context mContext;
    private SuccessDialog successDialog;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar);
        cvBack = findViewById(R.id.cvBack);
        tvLeftTitle = findViewById(R.id.tvLeftTitle);
        tvRightTitle = findViewById(R.id.tvRightTitle);
        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etPassword = findViewById(R.id.etPassword);
        showPasswordCheckbox = findViewById(R.id.showPasswordCheckbox);
        cvLogin = findViewById(R.id.cvLogin);

        setSupportActionBar(toolbar);

        mContext = this;

        mAuth = FirebaseAuth.getInstance();
        appDatabase = MyApp.getAppDatabase();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show Password
                etPassword.setTransformationMethod(null);
            } else {
                // Hide Password
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        tvLeftTitle.setText(getString(R.string.login));
        tvRightTitle.setVisibility(View.INVISIBLE);
        cvBack.setOnClickListener(v -> {
            finish();
        });

        cvLogin.setOnClickListener(v -> {
            if (etEmail.getText().toString().isEmpty()) {
                etEmail.setError("Email tidak boleh kosong");
                return;
            }
            if (etPassword.getText().toString().isEmpty()) {
                etPassword.setError("Password tidak boleh kosong");
                return;
            }

            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            doSignIn();
        });
    }

    public void doSignIn() {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    boolean emailVerified = user.isEmailVerified();
                    if (emailVerified) {
                        String name = user.getDisplayName();
                        String email = user.getEmail();
                        Log.d(TAG, "signInWithEmail:name : " + name);
                        Log.d(TAG, "signInWithEmail:email : " + email);

                        Log.d(TAG, "signInWithEmail:emailVerified : " + emailVerified);

                        String uid = user.getUid();
                        successDialog = new SuccessDialog(mContext, "Login Berhasil", "", new SuccessDialog.SuccessCallback() {
                            @Override
                            public void onClick() {
                                Intent intent = new Intent(mContext, MenuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        successDialog.show();
                    } else {
                        failedDialog = new FailedDialog(mContext, "Login Gagal", "Mohon untuk melakukan verifikasi email terlebih dahulu");

                        failedDialog.show();
                    }
                } else {
                    String exceptionMessage = task.getException().getMessage();
                    Log.e(TAG, "signInWithEmail:failure (" + task.getException().getMessage() + ")", task.getException());
                    if (exceptionMessage != null) {
                        if (exceptionMessage.contains("The supplied auth credential is incorrect")) {
                            failedDialog = new FailedDialog(mContext, "Login Gagal", "Email dan password tidak sesuai");
                        } else {
                            failedDialog = new FailedDialog(mContext, "Login Gagal", "Terjadi kesalahan");
                        }
                    } else {
                        failedDialog = new FailedDialog(mContext, "Login Gagal", "Terjadi kesalahan");
                    }
                    failedDialog.show();
                }
            }
        });
    }
}