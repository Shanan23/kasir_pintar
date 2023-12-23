package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.Outlets;

@Dao
public interface OutletsDao {
    @Insert
    long insertOutlets(Outlets outlets);

    @Upsert
    long upsertOutlets(Outlets outlets);

    @Query("SELECT * FROM Outlets")
    List<Outlets> getAllOutlets();

    @Query("SELECT * FROM Outlets WHERE id = :outletId")
    Outlets getAllOutletsById(String outletId);

    @Query("SELECT * FROM Outlets WHERE created_at BETWEEN :startDate AND :endDate")
    List<Outlets> getAllOutletsFilter(String startDate, String endDate);
}
