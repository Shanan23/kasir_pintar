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

    @Query("SELECT * FROM Users WHERE outlet_id = :shopId")
    List<Users> getAllUsersByOutlet(String shopId);

    @Query("SELECT * FROM Users WHERE id = :uid LIMIT 1")
    Users getUserById(String uid);

    @Query("SELECT * FROM Users WHERE email = :email LIMIT 1")
    Users getUserByEmail(String email);

    @Query("SELECT * FROM Users WHERE email = :email AND pin = :pin LIMIT 1")
    Users getUserByPin(String email, String pin);
}