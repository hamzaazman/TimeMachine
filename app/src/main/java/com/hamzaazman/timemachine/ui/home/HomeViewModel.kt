package com.hamzaazman.timemachine.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.timemachine.delegation.MVI
import com.hamzaazman.timemachine.delegation.mvi
import com.hamzaazman.timemachine.domain.usecase.GetOnThisDayEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getEvents: GetOnThisDayEventsUseCase
) : ViewModel(),
    MVI<HomeContract.UiState, HomeContract.UiAction, HomeContract.UiEffect> by mvi(HomeContract.UiState()) {


    init {
        onAction(HomeContract.UiAction.OnStart)
    }

    override fun onAction(uiAction: HomeContract.UiAction) {
        when (uiAction) {
            HomeContract.UiAction.OnStart -> {
                loadFor(currentUiState.selected, force = false)
            }

            HomeContract.UiAction.Refresh -> {
                loadFor(currentUiState.selected, force = true)
            }

            is HomeContract.UiAction.SelectDate -> {
                val safeDate = clampToToday(uiAction.date)
                loadFor(safeDate, force = false)
            }

            HomeContract.UiAction.PrevDay -> {
                val previousDay = currentUiState.selected.minus(1, DateTimeUnit.DAY)
                loadFor(previousDay, force = false)
            }

            HomeContract.UiAction.NextDay -> {
                if (currentUiState.canGoNext) {
                    val nextDay = currentUiState.selected.plus(1, DateTimeUnit.DAY)
                    val safeNextDay = clampToToday(nextDay)
                    loadFor(safeNextDay, force = false)
                }
            }

            is HomeContract.UiAction.ClickEvent -> {
                viewModelScope.launch {
                    uiAction.url?.let {
                        emitUiEffect(HomeContract.UiEffect.OpenUrl(it))
                    } ?: emitUiEffect(HomeContract.UiEffect.ShowMessage("Bağlantı bulunamadı"))
                }
            }
        }
    }

    private fun loadFor(date: LocalDate, force: Boolean) {
        val safeDate = clampToToday(date)
        updateUiState {
            copy(
                isLoading = true,
                selected = safeDate,
                error = null
            )
        }

        val month = safeDate.month.number
        val day = safeDate.day

        viewModelScope.launch {
            runCatching {
                getEvents(month, day, force)
            }
                .onSuccess { eventList ->
                    updateUiState {
                        copy(
                            isLoading = false,
                            events = eventList,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    val errorMessage = exception.message ?: "Bir hata oluştu"
                    updateUiState {
                        copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                    emitUiEffect(HomeContract.UiEffect.ShowMessage(errorMessage))
                }
        }
    }

    private fun clampToToday(input: LocalDate): LocalDate {
        val today = Clock.System.todayIn(TimeZone.UTC)
        return if (input > today) today else input
    }
}

fun LocalDate.formatAsTitle(): String {
    val monthNames = listOf(
        "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
        "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"
    )
    return "$day ${monthNames[month.number - 1]} ${this.year}"
}
