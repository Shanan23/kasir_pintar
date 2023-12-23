package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.Buy;

@Dao
public interface BuyDao {
    @Insert
    long insertBuy(Buy buy);

    @Upsert
    long upsertBuy(Buy buy);

    @Query("SELECT * FROM Buy")
    List<Buy> getAllBuy();

    @Query("SELECT * FROM Buy WHERE id_outlet = :outletId AND created_at BETWEEN :startDate AND :endDate")
    List<Buy> getAllBuyFilter(String startDate, String endDate, String outletId);
}