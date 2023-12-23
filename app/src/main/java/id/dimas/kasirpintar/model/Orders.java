package id.dimas.kasirpintar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class Orders implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "id_outlet")
    public String idOutlet;
    @ColumnInfo(name = "customer_id")
    public String customerId;
    @ColumnInfo(name = "amount")
    public int amount;
    @ColumnInfo(name = "pay_amount")
    public int payAmount;
    @ColumnInfo(name = "different")
    public int different;
    @ColumnInfo(name = "profit")
    public int profit;
    @ColumnInfo(name = "order_date")
    public String orderDate;
    @ColumnInfo(name = "order_status")
    public String orderStatus;
    @ColumnInfo(name = "paid_at")
    public String paidAt;
    @ColumnInfo(name = "created_at")
    public String createdAt;
    @ColumnInfo(name = "deleted_at")
    public String deletedAt;

    @Ignore
    public List<OrdersDetail> ordersDetailList;
    @Ignore
    public Profit profitItem;

    public Profit getProfitItem() {
        return profitItem;
    }

    public void setProfitItem(Profit profitItem) {
        this.profitItem = profitItem;
    }

    public List<OrdersDetail> getOrdersDetailList() {
        return ordersDetailList;
    }

    public void setOrdersDetailList(List<OrdersDetail> ordersDetailList) {
        this.ordersDetailList = ordersDetailList;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public int getDifferent() {
        return different;
    }

    public void setDifferent(int different) {
        this.different = different;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }
}
