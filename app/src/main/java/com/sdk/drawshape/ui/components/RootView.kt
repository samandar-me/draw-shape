package com.sdk.drawshape.ui.components

import android.view.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.sdk.drawshape.ui.theme.DrawShapeTheme
import com.sdk.drawshape.ui.theme.isSystemInDarkThemeCustom
import com.sdk.drawshape.ui.theme.StatusBarConfig

@Composable
fun Root(window: Window, content: @Composable () -> Unit) {
    val isDark = isSystemInDarkThemeCustom()
    DrawShapeTheme(isDark) {
        window.StatusBarConfig(isDark)
        Surface(color = MaterialTheme.colors.background) {
            content.invoke()
        }
    }
}