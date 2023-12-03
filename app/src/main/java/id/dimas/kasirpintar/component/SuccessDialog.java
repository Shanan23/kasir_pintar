package id.dimas.kasirpintar.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.dimas.kasirpintar.R;

public class SuccessDialog extends Dialog {

    Context context;
    String title;
    String message;

    public SuccessDialog(Context context, String title, String message) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_success);


        // Customize the dialog components
        ImageView successIcon = findViewById(R.id.successIcon);
        TextView successTitle = findViewById(R.id.successTitle);
        TextView successMessage = findViewById(R.id.successMessage);
        CardView cvOk = findViewById(R.id.cvOk);

        // You can customize the icon, title, and message here
        successIcon.setImageResource(R.drawable.ic_success);
        successTitle.setText(title);
        successMessage.setText(message);

        // Set click listener if needed
        cvOk.setOnClickListener(v -> dismiss());
    }

    @Override
    public void show() {
        super.show();
    }
}