package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.Customers;

@Dao
public interface CustomersDao {
    @Insert
    long insertCustomers(Customers customers);
    @Upsert
    long upsertCustomers(Customers customers);
    @Query("SELECT * FROM Customers")
    List<Customers> getAllCustomers();
}