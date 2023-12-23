package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.Categories;

@Dao
public interface CategoriesDao {
    @Insert
    long insertCategories(Categories categories);

    @Upsert
    long upsertCategories(Categories categories);

    @Query("SELECT * FROM Categories")
    List<Categories> getAllCategories();

    @Query("SELECT name FROM Categories")
    List<String> getAllCategoriesName();

    @Query("SELECT * FROM Categories WHERE id_outlet = :shopId")
    List<Categories> getAllCategoriesById(String shopId);
}