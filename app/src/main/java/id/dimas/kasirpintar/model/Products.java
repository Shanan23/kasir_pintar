package id.dimas.kasirpintar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Products {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "sku")
    public String sku;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "price")
    public String price;
    @ColumnInfo(name = "descriptions")
    public String descriptions;
    @ColumnInfo(name = "cid")
    public String cid;
    @ColumnInfo(name = "create_date")
    public String createDate;
    @ColumnInfo(name = "stock")
    public int stock;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] image;
    @ColumnInfo(name = "created_at")
    public String createdAt;
    @ColumnInfo(name = "deleted_at")
    public String deletedAt;
}
