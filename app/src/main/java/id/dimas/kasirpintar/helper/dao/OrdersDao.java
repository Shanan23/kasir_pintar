package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.Profit;

@Dao
public interface OrdersDao {
    @Insert
    long insertOrders(Orders orders);
    @Upsert
    long upsertOrders(Orders orders);
    @Query("SELECT * FROM Orders")
    List<Orders> getAllOrders();
    @Query("SELECT * FROM Orders WHERE id_outlet = :shopId")
    List<Orders> getAllOrdersById(String shopId);

    @Query("SELECT * FROM Orders WHERE order_status = :status")
    List<Orders> getAllOrdersByStatus(String status);

    @Query("SELECT SUM(profit) as profit, 0 as totalItem, SUM(amount) as total FROM Orders")
    Profit getAllProfit();

    @Query("SELECT * FROM Orders ORDER BY id DESC LIMIT 1")
    Orders getLastOrders();
}