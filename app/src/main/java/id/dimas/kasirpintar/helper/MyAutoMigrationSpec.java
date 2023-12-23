package id.dimas.kasirpintar.helper;

import androidx.annotation.NonNull;
import androidx.room.DeleteTable;
import androidx.room.migration.AutoMigrationSpec;
import androidx.sqlite.db.SupportSQLiteDatabase;


@DeleteTable(tableName = "Customers")
public class MyAutoMigrationSpec implements AutoMigrationSpec {
    @Override
    public void onPostMigrate(@NonNull SupportSQLiteDatabase database) {
        // Delete the table if it exists
        database.execSQL("DROP TABLE IF EXISTS Customers");
    }
}