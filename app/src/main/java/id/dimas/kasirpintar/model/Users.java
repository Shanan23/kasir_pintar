package id.dimas.kasirpintar.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Users implements Serializable {
    @NonNull
    @PrimaryKey
    public String id;
    @ColumnInfo(name = "outlet_id")
    public String outletId;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "is_active", defaultValue = "false")
    public boolean isActive;
    @ColumnInfo(name = "is_customer", defaultValue = "false")
    public boolean isCustomer;
    @ColumnInfo(name = "is_admin", defaultValue = "false")
    public boolean isAdmin;
    @ColumnInfo(name = "is_verification_send", defaultValue = "false")
    public boolean isVerificationSend;
    @ColumnInfo(name = "pin")
    public String pin;
    @ColumnInfo(name = "created_at")
    public String createdAt;
    @ColumnInfo(name = "deleted_at")
    public String deletedAt;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isVerificationSend() {
        return isVerificationSend;
    }

    public void setVerificationSend(boolean verificationSend) {
        isVerificationSend = verificationSend;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }
}