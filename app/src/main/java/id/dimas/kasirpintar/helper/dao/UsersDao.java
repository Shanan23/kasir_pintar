package id.dimas.kasirpintar.helper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import id.dimas.kasirpintar.model.Users;

@Dao
public interface UsersDao {
    @Insert
    long insertUsers(Users users);

    @Upsert
    long upsertUsers(Users users);

    @Query("SELECT * FROM Users")
    List<Users> getAllUsers();

    @Query("SELECT * FROM Users WHERE id = :uid")
    Users getUserById(String uid);
}