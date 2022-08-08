package com.sdk.drawshape.local

import android.Manifest
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val themePreferenceKey = intPreferencesKey("list_theme")

internal val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
internal const val PERMISSION_CODE = 100

fun Context.isDarkThemeOn() = dataStore.data
    .map { preferences ->
        preferences[themePreferenceKey] ?: 0
    }

fun Color.convertToOldColor(): Int {
    val color = this.toArgb()
    return android.graphics.Color.argb(
        color.alpha,
        color.red,
        color.green,
        color.blue
    )
}