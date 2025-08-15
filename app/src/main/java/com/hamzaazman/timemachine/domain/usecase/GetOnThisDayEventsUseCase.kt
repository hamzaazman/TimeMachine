package com.hamzaazman.timemachine.domain.usecase

import com.hamzaazman.timemachine.domain.model.HistoryEvent
import com.hamzaazman.timemachine.domain.repository.HistoryRepository
import javax.inject.Inject

class GetOnThisDayEventsUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(month: Int, day: Int, force: Boolean): List<HistoryEvent> =
        repository.getEvents(month, day, force)
}
