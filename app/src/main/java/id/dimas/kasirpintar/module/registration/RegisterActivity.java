package id.dimas.kasirpintar.module.registration;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.component.FailedDialog;
import id.dimas.kasirpintar.component.SuccessDialog;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private final String TAG = "RegisterActivity";

    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private CardView cvDaftar;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set the Toolbar as the ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        etEmail = (TextInputEditText) findViewById(R.id.etEmail);
        etPassword = (TextInputEditText) findViewById(R.id.etPassword);
        cvDaftar = (CardView) findViewById(R.id.cvDaftar);

        mContext = this;

        cvDaftar.setOnClickListener(v -> {
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

                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(RegisterActivity.this, (OnCompleteListener<Void>) task1 -> {
                            if (task1.isSuccessful()) {
                                // Email sent successfully
                                SuccessDialog successDialog = new SuccessDialog(mContext, "Registrasi Berhasil", getString(R.string.check_email));
                                successDialog.show();
                            } else {
                                // If verification email fails to send
                                Toast.makeText(mContext, "Failed to send verification email", Toast.LENGTH_SHORT).show();

                                SuccessDialog successDialog = new SuccessDialog(mContext, "Registrasi Berhasil", getString(R.string.check_email));
                                successDialog.show();
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
//            reload();
        }
    }
}