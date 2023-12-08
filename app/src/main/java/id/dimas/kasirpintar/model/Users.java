package id.dimas.kasirpintar.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Users {
    @NonNull
    @PrimaryKey
    public String id;
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

}