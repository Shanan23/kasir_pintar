package id.dimas.kasirpintar;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.dimas.kasirpintar.helper.AppDatabase;

public class MyApp extends Application {

    private static AppDatabase appDatabase;
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Room database instance
        appDatabase = AppDatabase.getDatabase(this);

    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
