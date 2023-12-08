package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.Products;

@Dao
public interface ProductsDao {
    @Insert
    long insertProducts(Products products);
    @Upsert
    long upsertProducts(Products products);
    @Query("SELECT * FROM Products")
    List<Products> getAllProducts();
}