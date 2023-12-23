package id.dimas.kasirpintar.helper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import id.dimas.kasirpintar.helper.dao.BuyDao;
import id.dimas.kasirpintar.helper.dao.CategoriesDao;
import id.dimas.kasirpintar.helper.dao.OrdersDao;
import id.dimas.kasirpintar.helper.dao.OrdersDetailDao;
import id.dimas.kasirpintar.helper.dao.OutletsDao;
import id.dimas.kasirpintar.helper.dao.ProductsDao;
import id.dimas.kasirpintar.helper.dao.UsersDao;
import id.dimas.kasirpintar.model.Buy;
import id.dimas.kasirpintar.model.Categories;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.Outlets;
import id.dimas.kasirpintar.model.Products;
import id.dimas.kasirpintar.model.Users;

@Database(entities = {Users.class, Categories.class, Buy.class, Orders.class, OrdersDetail.class, Products.class, Outlets.class}, version = 8, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoriesDao categoriesDao();

    public abstract OrdersDao ordersDao();

    public abstract OrdersDetailDao ordersDetailDao();

    public abstract ProductsDao productsDao();

    public abstract UsersDao usersDao();

    public abstract BuyDao buyDao();
    public abstract OutletsDao outletsDao();

    private static volatile AppDatabase INSTANCE;
    static SharedPreferenceHelper sharedPreferenceHelper;


    public static AppDatabase getDatabase(final Context context) {
        sharedPreferenceHelper = new SharedPreferenceHelper(context);
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "kasir_pintar_database.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Database opened
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Database created for the first time
        }

        @Override
        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
            super.onDestructiveMigration(db);

            sharedPreferenceHelper.setLoggedIn(false);
            sharedPreferenceHelper.setIsSavedPin(false);
            sharedPreferenceHelper.saveUsername("");
        }
    };
}

