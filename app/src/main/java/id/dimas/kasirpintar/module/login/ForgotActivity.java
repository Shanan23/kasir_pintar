package id.dimas.kasirpintar.module.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import id.dimas.kasirpintar.R;

public class ForgotActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonReset;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonReset = (Button) findViewById(R.id.buttonReset);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    // Send password reset email
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotActivity.this,
                                                "Password reset email sent",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ForgotActivity.this,
                                                "Failed to send reset email",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}