package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.OrdersDetail;
import id.dimas.kasirpintar.model.ReportTrxItem;

@Dao
public interface OrdersDetailDao {
    @Insert
    long insertOrdersDetail(OrdersDetail ordersdetail);

    @Upsert
    long upsertOrdersDetail(OrdersDetail ordersdetail);

    @Query("SELECT * FROM OrdersDetail")
    List<OrdersDetail> getAllOrdersDetail();

    @Query("SELECT * FROM OrdersDetail WHERE order_id = :orderId AND order_date BETWEEN :startDate AND :endDate")
    List<OrdersDetail> getAllOrdersDetailById(int orderId, String startDate, String endDate);

    @Query("SELECT item_id as id, name, SUM(qty) as totalQty FROM OrdersDetail WHERE order_date BETWEEN :startDate AND :endDate GROUP BY item_id")
    List<ReportTrxItem> getAllItemTrx(String startDate, String endDate);
}