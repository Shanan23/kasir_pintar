package id.dimas.kasirpintar.module.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.helper.SharedPreferenceHelper;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferenceHelper preferencesHelper = new SharedPreferenceHelper(this);

        // Check if the user is logged in
        boolean isLoggedIn = preferencesHelper.isLoggedIn();

        if(isLoggedIn){

        }

        // Retrieve username
        String username = preferencesHelper.getUsername();
    }
}