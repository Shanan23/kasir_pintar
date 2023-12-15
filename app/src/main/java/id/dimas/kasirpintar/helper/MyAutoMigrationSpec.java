package id.dimas.kasirpintar.helper;

import androidx.room.DeleteTable;
import androidx.room.migration.AutoMigrationSpec;

@DeleteTable(tableName = "Customers")
public class MyAutoMigrationSpec implements AutoMigrationSpec {
}