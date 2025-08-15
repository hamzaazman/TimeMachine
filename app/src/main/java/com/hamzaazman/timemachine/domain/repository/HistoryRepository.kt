package com.hamzaazman.timemachine.domain.repository

import com.hamzaazman.timemachine.domain.model.HistoryEvent

interface HistoryRepository {
    suspend fun getEvents(month: Int, day: Int, force: Boolean): List<HistoryEvent>
}