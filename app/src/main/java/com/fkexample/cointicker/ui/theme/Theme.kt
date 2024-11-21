package com.fkexample.cointicker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.fkexample.cointicker.ui.ConnectivityMonitor

@Composable
fun CoinTickerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isNetworkAvailable: Boolean,
    content: @Composable () -> Unit
) {

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column {
            ConnectivityMonitor(isNetworkAvailable = isNetworkAvailable)
            content()
        }
    }
}
