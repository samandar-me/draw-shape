package com.sdk.drawshape.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.sdk.drawshape.MainActivity
import com.sdk.drawshape.drawbox.DrawBox
import com.sdk.drawshape.drawbox.rememberDrawController
import com.sdk.drawshape.local.convertToOldColor
import com.sdk.drawshape.ui.components.ControlsBar
import com.sdk.drawshape.ui.components.CustomSeekbar
import com.sdk.drawshape.ui.theme.Purple500
import io.ak1.rangvikalp.RangVikalp
import io.ak1.rangvikalp.defaultSelectedColor


@Composable
fun HomeScreen(save: (Bitmap) -> Unit) {
    val activity = (LocalContext.current as? MainActivity)
    val undoVisibility = remember { mutableStateOf(false) }
    val redoVisibility = remember { mutableStateOf(false) }
    val colorBarVisibility = remember { mutableStateOf(false) }
    val sizeBarVisibility = remember { mutableStateOf(false) }
    val currentColor = remember { mutableStateOf(defaultSelectedColor) }
    val bg = MaterialTheme.colors.background
    val currentBgColor = remember { mutableStateOf(bg) }
    val currentSize = remember { mutableStateOf(10) }
    val colorIsBg = remember { mutableStateOf(false) }
    val drawController = rememberDrawController()
    val openDialog = remember { mutableStateOf(false) }

    Box {
        Column {
            DrawBox(
                drawController = drawController,
                backgroundColor = currentBgColor.value,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, fill = false),
                bitmapCallback = { imageBitmap, _ ->
                    imageBitmap?.let {
                        save(it.asAndroidBitmap())
                    }
                }
            ) { undoCount, redoCount ->
                sizeBarVisibility.value = false
                colorBarVisibility.value = false
                undoVisibility.value = undoCount != 0
                redoVisibility.value = redoCount != 0
            }

            ControlsBar(
                drawController = drawController,
                {
                    drawController.saveBitmap()
                },
                {
                    colorBarVisibility.value = when (colorBarVisibility.value) {
                        false -> true
                        colorIsBg.value -> true
                        else -> false
                    }
                    colorIsBg.value = false
                    sizeBarVisibility.value = false
                },
                {
                    colorBarVisibility.value = when (colorBarVisibility.value) {
                        false -> true
                        !colorIsBg.value -> true
                        else -> false
                    }
                    colorIsBg.value = true
                    sizeBarVisibility.value = false
                },
                {
                    sizeBarVisibility.value = !sizeBarVisibility.value
                    colorBarVisibility.value = false
                },
                undoVisibility = undoVisibility,
                redoVisibility = redoVisibility,
                colorValue = currentColor,
                bgColorValue = currentBgColor,
                sizeValue = currentSize
            )
            RangVikalp(isVisible = colorBarVisibility.value, showShades = true) {
                if (colorIsBg.value) {
                    currentBgColor.value = it
                    drawController.changeBgColor(it)
                } else {
                    currentColor.value = it
                    drawController.changeColor(it)
                }
            }
            CustomSeekbar(
                isVisible = sizeBarVisibility.value,
                progress = currentSize.value,
                progressColor = MaterialTheme.colors.primary.convertToOldColor(),
                thumbColor = currentColor.value.convertToOldColor()
            ) {
                currentSize.value = it
                drawController.changeStrokeWidth(it.toFloat())
            }
        }
    }
    BackHandler {
        if (drawController.pathList.size != 0)
            openDialog.value = true
        else
            activity?.finish()
    }
    if (openDialog.value) {
        MaterialTheme {
            Column {
                AlertDialog(onDismissRequest = {
                    openDialog.value = false
                },
                    title = { Text(text = "Save") },
                    text = { Text(text = "Save changes to gallery?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                                drawController.saveBitmap()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Purple500)
                        ) {
                            Text(text = "Yes", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                                activity?.finish()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Purple500)
                        ) {
                            Text(text = "No", color = Color.White)
                        }
                    }
                )
            }
        }
    }
}
