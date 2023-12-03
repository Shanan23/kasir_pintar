package id.dimas.kasirpintar.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import id.dimas.kasirpintar.R;

public class FailedDialog extends Dialog {

    Context context;
    String title;
    String message;

    public FailedDialog(Context context, String title, String message) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_failed);


        // Customize the dialog components
        ImageView successIcon = findViewById(R.id.failedIcon);
        TextView successTitle = findViewById(R.id.failedTitle);
        TextView successMessage = findViewById(R.id.failedMessage);
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