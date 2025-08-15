package com.hamzaazman.timemachine.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hamzaazman.timemachine.data.source.local.entities.HistoryEventEntity

@Dao
interface HistoryEventDao {
    @Query("SELECT * FROM history_events WHERE dateKey = :dateKey ORDER BY year ASC")
    suspend fun getByDate(dateKey: String): List<HistoryEventEntity>

    @Query("DELETE FROM history_events WHERE dateKey = :dateKey")
    suspend fun deleteByDate(dateKey: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<HistoryEventEntity>)

    @Transaction
    suspend fun replaceForDate(dateKey: String, list: List<HistoryEventEntity>) {
        deleteByDate(dateKey)
        insertAll(list)
    }
}