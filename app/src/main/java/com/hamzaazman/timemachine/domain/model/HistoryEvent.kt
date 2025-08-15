package com.hamzaazman.timemachine.domain.model

import com.hamzaazman.timemachine.data.model.HistoricalEventDto

data class HistoryEvent(
    val id: String,
    val year: Int,
    val text: String,
    val imageUrl: String?,
    val url: String?
)

fun HistoricalEventDto.toDomain(dateKey: String): HistoryEvent {
    val firstPage = pages?.firstOrNull()
    val image = firstPage?.thumbnail?.source ?: firstPage?.originalImage?.source
    val link = firstPage?.contentUrls?.mobile?.page
        ?: firstPage?.contentUrls?.desktop?.page
    val stableId = "$dateKey-$year-${text.hashCode()}"
    return HistoryEvent(
        id = stableId,
        year = year,
        text = text,
        imageUrl = image,
        url = link
    )
}
