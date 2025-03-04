package com.p2.apps.mygotraintracker.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.p2.apps.mygotraintracker.presentation.utils.ViewState

@Composable
fun <T> LoadingContent(
    state: ViewState<T>,
    emptyContent: @Composable () -> Unit = { DefaultEmptyContent() },
    errorContent: @Composable (Throwable) -> Unit = { DefaultErrorContent(it) },
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    content: @Composable (T) -> Unit
) {
    when (state) {
        is ViewState.Loading -> {
            loadingContent()
        }
        is ViewState.RenderSuccess -> {
            if (state.data == null || (state.data is List<*> && (state.data as List<*>).isEmpty())) {
                emptyContent()
            } else {
                content(state.data)
            }
        }
        is ViewState.RenderFailure -> {
            errorContent(state.throwable)
        }
    }
}

@Composable
fun DefaultLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun DefaultEmptyContent(message: String = "No data available") {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DefaultErrorContent(error: Throwable) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: ${error.message ?: "Unknown error"}",
            textAlign = TextAlign.Center
        )
    }
} 