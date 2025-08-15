package com.hamzaazman.timemachine.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hamzaazman.timemachine.common.Utils.openInCustomTab
import com.hamzaazman.timemachine.common.collectWithLifecycle
import com.hamzaazman.timemachine.ui.components.AnimatedTopBar
import com.hamzaazman.timemachine.ui.components.DateNavigationRow
import com.hamzaazman.timemachine.ui.components.EmptyStateCard
import com.hamzaazman.timemachine.ui.components.ErrorCard
import com.hamzaazman.timemachine.ui.components.EventCardCompact
import com.hamzaazman.timemachine.ui.components.WorkingDatePicker
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate


@Composable
fun HomeScreen(
    uiState: HomeContract.UiState,
    uiEffect: Flow<HomeContract.UiEffect>,
    onAction: (HomeContract.UiAction) -> Unit,
) {
    val context = LocalContext.current

    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is HomeContract.UiEffect.OpenUrl -> {
                openInCustomTab(context, effect.url)
            }

            is HomeContract.UiEffect.ShowMessage -> Toast.makeText(
                context,
                effect.msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    HomeContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onAction = onAction,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeContract.UiState,
    onAction: (HomeContract.UiAction) -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


    val onPrevDay = remember { { onAction(HomeContract.UiAction.PrevDay) } }
    val onNextDay = remember { { onAction(HomeContract.UiAction.NextDay) } }
    val onRefresh = remember { { onAction(HomeContract.UiAction.Refresh) } }
    val onDateClick = remember { { showPicker = true } }
    val onDateSelected = remember {
        { selectedDate: LocalDate -> onAction(HomeContract.UiAction.SelectDate(selectedDate)) }
    }
    val onDismissPicker = remember { { showPicker = false } }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedTopBar(
                scrollBehavior = scrollBehavior,
                isLoading = uiState.isLoading,
                onRefresh = onRefresh
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        ) {

            stickyHeader(key = "date_navigation") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    shadowElevation = if (scrollBehavior.state.collapsedFraction > 0.5f) 4.dp else 0.dp
                ) {
                    DateNavigationRow(
                        selectedDate = uiState.selected,
                        canGoNext = uiState.canGoNext,
                        onPrevDay = onPrevDay,
                        onNextDay = onNextDay,
                        onDateClick = onDateClick,
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
                    )
                }
            }

            // Loading State
            if (uiState.isLoading) {
                item(key = "loading") {
                    LoadingIndicator(
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

            // Events List
            items(
                items = uiState.events,
                key = { it.id }
            ) { event ->
                EventCardCompact(
                    item = event,
                    onClick = { onAction(HomeContract.UiAction.ClickEvent(event.url)) },
                )
            }


            //Error State
            uiState.error?.let { error ->
                item(key = "error") {
                    ErrorCard(
                        message = error,
                        onRetry = onRefresh
                    )
                }
            }

            // Empty State
            if (uiState.events.isEmpty() && !uiState.isLoading && uiState.error == null) {
                item(key = "empty_state") {
                    EmptyStateCard(
                        selectedDate = uiState.selected,
                        onSelectDifferentDate = onDateClick
                    )
                }
            }


            if (uiState.events.isNotEmpty()) {
                item(key = "bottom_spacing") {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }


    if (showPicker) {
        WorkingDatePicker(
            isVisible = true,
            onDateSelected = onDateSelected,
            onDismiss = onDismissPicker
        )
    }

}


@Composable
private fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp
            )
            Text(
                text = "Tarihsel olaylar yükleniyor...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

