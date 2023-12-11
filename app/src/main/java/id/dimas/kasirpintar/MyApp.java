package id.dimas.kasirpintar;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.dimas.kasirpintar.helper.AppDatabase;
import id.dimas.kasirpintar.helper.PrintHelper;

public class MyApp extends Application {

    private static AppDatabase appDatabase;
    private static PrintHelper printHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Room database instance
        appDatabase = AppDatabase.getDatabase(this);

        printHelper = new PrintHelper();
    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static PrintHelper getPrintHelper() {
        return printHelper;
    }

    public enum Status {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED
    }
}
