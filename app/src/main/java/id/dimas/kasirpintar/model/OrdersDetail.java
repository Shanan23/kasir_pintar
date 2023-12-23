package id.dimas.kasirpintar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class OrdersDetail implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "id_outlet")
    public String idOutlet;
    @ColumnInfo(name = "order_id")
    public int ordersId;
    @ColumnInfo(name = "item_id")
    public String itemId;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "qty")
    public int qty;
    @ColumnInfo(name = "total_details")
    public int totalDetails;
    @ColumnInfo(name = "order_date")
    public String orderDate;
    @ColumnInfo(name = "order_status")
    public String orderStatus;
    @ColumnInfo(name = "created_at")
    public String createdAt;
    @ColumnInfo(name = "deleted_at")
    public String deletedAt;

    @Ignore
    public Products products;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getTotalDetails() {
        return totalDetails;
    }

    public void setTotalDetails(int totalDetails) {
        this.totalDetails = totalDetails;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }
}
