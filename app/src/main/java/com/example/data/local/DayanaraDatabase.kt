package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ReportEntity::class, ChatMessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DayanaraDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: DayanaraDatabase? = null

        fun getDatabase(context: Context): DayanaraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DayanaraDatabase::class.java,
                    "dayanara_secure.db"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
