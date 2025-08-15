package com.hamzaazman.timemachine.data.repository

import com.hamzaazman.timemachine.data.source.local.dao.HistoryEventDao
import com.hamzaazman.timemachine.data.source.local.entities.toDomain
import com.hamzaazman.timemachine.data.source.local.entities.toEntity
import com.hamzaazman.timemachine.data.source.remote.WikimediaApiService
import com.hamzaazman.timemachine.di.IoDispatcher
import com.hamzaazman.timemachine.domain.model.HistoryEvent
import com.hamzaazman.timemachine.domain.model.toDomain
import com.hamzaazman.timemachine.domain.repository.HistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val api: WikimediaApiService,
    private val dao: HistoryEventDao,
    @IoDispatcher private val io: CoroutineDispatcher
) : HistoryRepository {

    private fun dateKey(month: Int, day: Int) = "%02d-%02d".format(month, day)

    override suspend fun getEvents(month: Int, day: Int, force: Boolean): List<HistoryEvent> =
        withContext(io) {
            val key = dateKey(month, day)
            val local = dao.getByDate(key)
            if (local.isNotEmpty() && !force) {
                return@withContext local.map { it.toDomain() }
            }

            val res = api.getOnThisDayEvents(
                language = "tr",
                type = "events", // ilk sürümde sadece events
                month = "%02d".format(month),
                day = "%02d".format(day)
            )

            if (!res.isSuccessful) {
                if (local.isNotEmpty()) return@withContext local.map { it.toDomain() }
                error("Ağ hatası: ${res.code()}")
            }

            val dto = res.body()?.events.orEmpty()
            val mapped = dto.map { it.toDomain(key) }.sortedBy { it.year }
            dao.replaceForDate(key, mapped.map { it.toEntity(key) })
            mapped
        }
}