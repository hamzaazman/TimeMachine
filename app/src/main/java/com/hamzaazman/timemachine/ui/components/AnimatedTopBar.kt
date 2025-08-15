package com.hamzaazman.timemachine.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val collapsedFraction = scrollBehavior.state.collapsedFraction

    val titleScale by animateFloatAsState(
        targetValue = lerp(1f, 0.85f, collapsedFraction),
        animationSpec = tween(durationMillis = 150),
        label = "title_scale"
    )
    
    // Title alpha animation - fade effect
    val titleAlpha by animateFloatAsState(
        targetValue = lerp(1f, 0.9f, collapsedFraction),
        animationSpec = tween(durationMillis = 150),
        label = "title_alpha"
    )

    LargeTopAppBar(
        title = { 
            Text(
                text = "Bugün Geçmişte Ne Oldu?",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .scale(titleScale)
                    .alpha(titleAlpha),
                maxLines = if (collapsedFraction > 0.5f) 1 else 2,
                overflow = TextOverflow.Ellipsis
            ) 
        },
        actions = {
            IconButton(
                onClick = onRefresh,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Yenile"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = modifier
    )
}