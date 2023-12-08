package id.dimas.kasirpintar.helper;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import id.dimas.kasirpintar.helper.dao.CategoriesDao;
import id.dimas.kasirpintar.helper.dao.CustomersDao;
import id.dimas.kasirpintar.helper.dao.OrdersDao;
import id.dimas.kasirpintar.helper.dao.OrdersDetailDao;
import id.dimas.kasirpintar.helper.dao.ProductsDao;
import id.dimas.kasirpintar.helper.dao.UsersDao;
import id.dimas.kasirpintar.model.Categories;
import id.dimas.kasirpintar.model.Customers;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.Products;
import id.dimas.kasirpintar.model.Users;

@Database(entities = {Users.class, Categories.class, Customers.class, Orders.class, OrdersDetail.class, Products.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoriesDao categoriesDao();
    public abstract CustomersDao customersDao();
    public abstract OrdersDao ordersDao();
    public abstract OrdersDetailDao ordersDetailDao();
    public abstract ProductsDao productsDao();
    public abstract UsersDao usersDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "kasir_pintar_database.db").build();
                }
            }
        }
        return INSTANCE;
    }
}