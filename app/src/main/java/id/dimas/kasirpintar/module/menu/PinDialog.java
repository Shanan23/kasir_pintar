package id.dimas.kasirpintar.module.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.cardview.widget.CardView;

import id.dimas.kasirpintar.R;

public class PinDialog {

    public interface PinDialogListener {
        void onPinEntered(String pin);
    }

    public static void showPinDialog(Context context, PinDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_pin, null);

        EditText etPin = dialogView.findViewById(R.id.etPin);
        CardView cvOk = dialogView.findViewById(R.id.cvOk);
        cvOk.setOnClickListener(v -> {
            String enteredPin = etPin.getText().toString().trim();
            listener.onPinEntered(enteredPin);
        });

        builder.setView(dialogView)
                .setCancelable(false)
                .create()
                .show();
    }
}