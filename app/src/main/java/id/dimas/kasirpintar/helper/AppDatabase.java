package id.dimas.kasirpintar.helper;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.DeleteTable;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import id.dimas.kasirpintar.helper.dao.BuyDao;
import id.dimas.kasirpintar.helper.dao.CategoriesDao;
import id.dimas.kasirpintar.helper.dao.OrdersDao;
import id.dimas.kasirpintar.helper.dao.OrdersDetailDao;
import id.dimas.kasirpintar.helper.dao.ProductsDao;
import id.dimas.kasirpintar.helper.dao.UsersDao;
import id.dimas.kasirpintar.model.Buy;
import id.dimas.kasirpintar.model.Categories;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.Products;
import id.dimas.kasirpintar.model.Users;

@Database(entities = {Users.class, Categories.class, Buy.class, Orders.class, OrdersDetail.class, Products.class}, version = 3,
        autoMigrations = {
                @AutoMigration(from = 2, to = 3, spec = MyAutoMigrationSpec.class)
        }, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoriesDao categoriesDao();

    public abstract OrdersDao ordersDao();

    public abstract OrdersDetailDao ordersDetailDao();

    public abstract ProductsDao productsDao();

    public abstract UsersDao usersDao();

    public abstract BuyDao buyDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "kasir_pintar_database.db")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}

