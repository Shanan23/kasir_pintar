package id.dimas.kasirpintar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Orders {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "customer_id")
    public int customerId;
    @ColumnInfo(name = "amount")
    public int amount;
    @ColumnInfo(name = "order_date")
    public String orderDate;
    @ColumnInfo(name = "order_status")
    public String orderStatus;
    @ColumnInfo(name = "created_at")
    public String createdAt;
    @ColumnInfo(name = "deleted_at")
    public String deletedAt;
}
