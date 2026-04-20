package com.qainnovators.smartnotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Note::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE notes ADD COLUMN category TEXT NOT NULL DEFAULT 'Personal'")
                database.execSQL("ALTER TABLE notes ADD COLUMN color TEXT NOT NULL DEFAULT 'Default'")
                database.execSQL("ALTER TABLE notes ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE notes ADD COLUMN isFavourite INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE notes ADD COLUMN isLocked INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE notes ADD COLUMN lastEdited INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE notes ADD COLUMN isTrashed INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}