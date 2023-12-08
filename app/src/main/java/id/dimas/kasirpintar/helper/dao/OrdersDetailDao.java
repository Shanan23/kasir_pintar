package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.OrdersDetail;

@Dao
public interface OrdersDetailDao {
    @Insert
    long insertOrdersDetail(OrdersDetail ordersdetail);
    @Upsert
    long upsertOrdersDetail(OrdersDetail ordersdetail);
    @Query("SELECT * FROM OrdersDetail")
    List<OrdersDetail> getAllOrdersDetail();
}