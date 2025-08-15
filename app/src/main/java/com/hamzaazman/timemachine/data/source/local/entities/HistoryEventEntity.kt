package com.hamzaazman.timemachine.data.source.local.entities

import androidx.room.Entity
import com.hamzaazman.timemachine.domain.model.HistoryEvent

@Entity(tableName = "history_events", primaryKeys = ["dateKey", "id"])
data class HistoryEventEntity(
    val dateKey: String, // "08-14"
    val id: String,
    val year: Int,
    val text: String,
    val imageUrl: String?,
    val url: String?
)

fun HistoryEventEntity.toDomain() = HistoryEvent(id, year, text, imageUrl, url)
fun HistoryEvent.toEntity(dateKey: String) =
    HistoryEventEntity(dateKey, id, year, text, imageUrl, url)