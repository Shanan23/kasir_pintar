package id.dimas.kasirpintar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrdersDetail {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "order_id")
    public int ordersId;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "order_date")
    public String orderDate;
    @ColumnInfo(name = "order_status")
    public String orderStatus;
    @ColumnInfo(name = "created_at")
    public String createdAt;
    @ColumnInfo(name = "deleted_at")
    public String deletedAt;
}
