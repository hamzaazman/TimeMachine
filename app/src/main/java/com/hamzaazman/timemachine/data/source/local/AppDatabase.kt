package com.hamzaazman.timemachine.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hamzaazman.timemachine.data.source.local.dao.HistoryEventDao
import com.hamzaazman.timemachine.data.source.local.entities.HistoryEventEntity

@Database(entities = [HistoryEventEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryEventDao
}