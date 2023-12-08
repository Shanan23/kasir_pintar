package id.dimas.kasirpintar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Customers {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "user_id")
    public int userId;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "default_shipping_address")
    public String defaultShippingAddress;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "created_at")
    public String createdAt;
    @ColumnInfo(name = "deleted_at")
    public String deletedAt;
}
