package id.dimas.kasirpintar.module.security;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import id.dimas.kasirpintar.MyApp;
import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.component.FailedDialog;
import id.dimas.kasirpintar.component.SuccessDialog;
import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.HashUtils;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;
import id.dimas.kasirpintar.model.Users;
import id.dimas.kasirpintar.module.registration.VerificationActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecurityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private TextView lblPassword;
    private TextInputLayout tilOldPassword;
    private TextInputEditText etOldPassword;
    private TextInputLayout tilNewPassword;
    private TextInputEditText etNewPassword;
    private TextInputLayout tilConfirmPassword;
    private TextInputEditText etConfirmPassword;
    private CardView cvChangePassword;
    private View v1;
    private TextView lblPin;
    private TextInputLayout tilOldPin;
    private TextInputEditText etOldPin;
    private TextInputLayout tilNewPin;
    private TextInputEditText etNewPin;
    private TextInputLayout tilConfirmPin;
    private TextInputEditText etConfirmPin;
    private CardView cvChangePin;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private Context mContext;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);

        mContext = this;
        tvLeftTitle.setText("Keamanan");
        tvRightTitle.setVisibility(View.INVISIBLE);
        cvBack.setOnClickListener(v -> finish());
        appDatabase = MyApp.getAppDatabase();

        etOldPassword = (TextInputEditText) findViewById(R.id.etOldPassword);
        etNewPassword = (TextInputEditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (TextInputEditText) findViewById(R.id.etConfirmPassword);
        cvChangePassword = (CardView) findViewById(R.id.cvChangePassword);
        etOldPin = (TextInputEditText) findViewById(R.id.etOldPin);
        etNewPin = (TextInputEditText) findViewById(R.id.etNewPin);
        etConfirmPin = (TextInputEditText) findViewById(R.id.etConfirmPin);
        cvChangePin = (CardView) findViewById(R.id.cvChangePin);
        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);

        cvChangePassword.setOnClickListener(v -> {
            if (etOldPassword.getText().toString().isEmpty()) {
                etOldPassword.setError("Password lama tidak boleh kosong");
                return;
            }
            if (etNewPassword.getText().toString().isEmpty()) {
                etNewPassword.setError("Password baru tidak boleh kosong");
                return;
            }
            if (etConfirmPassword.getText().toString().isEmpty()) {
                etConfirmPassword.setError("Konfirmasi password baru tidak boleh kosong");
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // If the user is not null, proceed to change the password
            if (user != null) {
                // Assume the user is authenticated with email and password
                String email = sharedPreferenceHelper.getUsername(); // Replace with the user's email
                String currentPassword = etOldPassword.getText().toString(); // Replace with the user's current password
                String newPassword = etNewPassword.getText().toString(); // Replace with the new password

                // Create a credential using the user's email and current password
                AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

                // Reauthenticate the user with the provided credential
                user.reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // If reauthentication is successful, proceed to update the password
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(updateTask -> {
                                            if (updateTask.isSuccessful()) {
                                                // Password updated successfully
                                                // You may want to sign out the user or update UI accordingly

                                                SuccessDialog successDialog = new SuccessDialog(mContext, "Ganti Password Berhasil", "", () -> {
                                                    finish();
                                                });
                                                successDialog.show();
                                            } else {
                                                // Handle the error when updating the password
                                                FailedDialog failedDialog = new FailedDialog(mContext, "Ganti Password Gagal", "");
                                                failedDialog.show();
                                            }
                                        });
                            } else {
                                // Handle the error when reauthenticating the user
                                FailedDialog failedDialog = new FailedDialog(mContext, "Ganti Password Gagal", "");
                                failedDialog.show();
                            }
                        });
            }
        });

        cvChangePin.setOnClickListener(v -> {
            if (etOldPin.getText().toString().isEmpty()) {
                etOldPin.setError("Pin lama tidak boleh kosong");
                return;
            }
            if (etNewPin.getText().toString().isEmpty()) {
                etNewPin.setError("Pin baru tidak boleh kosong");
                return;
            }
            if (etConfirmPin.getText().toString().isEmpty()) {
                etConfirmPin.setError("Konfirmasi pin baru tidak boleh kosong");
                return;
            }

            Users users = appDatabase.usersDao().getUserByPin(sharedPreferenceHelper.getUsername(), HashUtils.hashPassword(etNewPin.getText().toString()));
            if (users != null) {
                users.pin = etNewPin.getText().toString();
                new Thread(() -> {
                    long upsertUsers = appDatabase.usersDao().upsertUsers(users);
                    if (upsertUsers > 0) {
                        Log.d("upsertUsers", "berhasil upsertUsers");
                        SuccessDialog successDialog = new SuccessDialog(mContext, "Ganti Pin Berhasil", "", () -> {
                            finish();
                        });
                        successDialog.show();
                    } else {
                        Log.e("upsertUsers", "gagal upsertUsers");
                        FailedDialog failedDialog = new FailedDialog(mContext, "Ganti Pin Gagal", "");
                        failedDialog.show();
                    }
                    finish();
                }).start();
            }
        });
    }
}