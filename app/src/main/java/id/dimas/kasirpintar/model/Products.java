package id.dimas.kasirpintar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Products implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "id_category")
    public String idCategory;
    @ColumnInfo(name = "id_outlet")
    public String idOutlet;
    @ColumnInfo(name = "sku")
    public String sku;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "buy_price")
    public String buyPrice;
    @ColumnInfo(name = "sell_price")
    public String sellPrice;
    @ColumnInfo(name = "is_stock")
    public String isStock;
    @ColumnInfo(name = "type")
    public String type;
    @ColumnInfo(name = "descriptions")
    public String descriptions;
    @ColumnInfo(name = "stock")
    public int stock;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] image;
    @ColumnInfo(name = "created_at")
    public String createdAt;
    @ColumnInfo(name = "deleted_at")
    public String deletedAt;
    @Ignore
    public int qty = 0;

    public int getQty() {

        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsStock() {
        return isStock;
    }

    public void setIsStock(String isStock) {
        this.isStock = isStock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }
}
