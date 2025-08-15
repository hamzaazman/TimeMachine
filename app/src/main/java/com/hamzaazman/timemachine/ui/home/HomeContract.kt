@file:OptIn(ExperimentalTime::class)

package com.hamzaazman.timemachine.ui.home

import com.hamzaazman.timemachine.domain.model.HistoryEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val selected: LocalDate = Clock.System.todayIn(TimeZone.UTC),
        val events: List<HistoryEvent> = emptyList(),
        val error: String? = null
    ) {

        val canGoNext: Boolean
            get() {
                val today = Clock.System.todayIn(TimeZone.UTC)
                return selected < today
            }
    }

    sealed interface UiAction {
        data object OnStart : UiAction
        data object Refresh : UiAction
        data class SelectDate(val date: LocalDate) : UiAction
        data object PrevDay : UiAction
        data object NextDay : UiAction
        data class ClickEvent(val url: String?) : UiAction
    }

    sealed interface UiEffect {
        data class OpenUrl(val url: String) : UiEffect
        data class ShowMessage(val msg: String) : UiEffect
    }
}