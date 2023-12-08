package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.Orders;

@Dao
public interface OrdersDao {
    @Insert
    long insertOrders(Orders orders);
    @Upsert
    long upsertOrders(Orders orders);
    @Query("SELECT * FROM Orders")
    List<Orders> getAllOrders();
}