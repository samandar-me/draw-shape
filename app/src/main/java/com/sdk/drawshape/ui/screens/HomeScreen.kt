package com.sdk.drawshape.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sdk.drawshape.app.MainActivity
import com.sdk.drawshape.drawbox.DrawBox
import com.sdk.drawshape.drawbox.colorutil.ColorPicker
import com.sdk.drawshape.drawbox.colorutil.defaultSelectedColor
import com.sdk.drawshape.drawbox.rememberDrawController
import com.sdk.drawshape.ui.components.ControlsBar
import com.sdk.drawshape.ui.components.CustomSeekbar
import com.sdk.drawshape.ui.theme.CustomDialog

@Composable
fun HomeScreen(save: (Bitmap) -> Unit) {
    val activity = (LocalContext.current as? MainActivity)
    val undoVisibility = rememberSaveable { mutableStateOf(false) }
    val redoVisibility = rememberSaveable { mutableStateOf(false) }
    val colorBarVisibility = remember { mutableStateOf(false) }
    val sizeBarVisibility = remember { mutableStateOf(false) }
    val currentColor = remember { mutableStateOf(defaultSelectedColor) }
    val bg = MaterialTheme.colors.background
    val currentBgColor = remember { mutableStateOf(bg) }
    val currentSize = rememberSaveable { mutableStateOf(10) }
    val colorIsBg = rememberSaveable { mutableStateOf(false) }
    val drawController = rememberDrawController()
    val openDialog = remember { mutableStateOf(false) }
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(currentBgColor.value)

    Box {
        Column {
            DrawBox(drawController = drawController,
                backgroundColor = currentBgColor.value,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, fill = false),
                bitmapCallback = { imageBitmap, _ ->
                    imageBitmap?.let {
                        save(it.asAndroidBitmap())
                    }
                }) { undoCount, redoCount ->
                sizeBarVisibility.value = false
                colorBarVisibility.value = false
                undoVisibility.value = undoCount != 0
                redoVisibility.value = redoCount != 0
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
            ) {
                ControlsBar(drawController = drawController,
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
//                    sizeValue = currentSize,
                    onBottomSwipe = {
                        colorBarVisibility.value = false
                        sizeBarVisibility.value = false
                    })
                ColorPicker(isVisible = colorBarVisibility.value,
                    showShades = true,
                    onBottomSwipe = {
                        colorBarVisibility.value = false
                    }) {
                    if (colorIsBg.value) {
                        currentBgColor.value = it
                        drawController.changeBgColor(it)
                    } else {
                        currentColor.value = it
                        drawController.changeColor(it)
                    }
                }
                CustomSeekbar(isVisible = sizeBarVisibility.value,
                    progress = currentSize.value,
                    thumbColor = currentColor.value,
                    onBottomSwipe = {
                        sizeBarVisibility.value = false
                    }) {
                    currentSize.value = it
                    drawController.changeStrokeWidth(it.toFloat())
                }
            }
        }
    }
    BackHandler {
        if (drawController.pathList.size != 0) openDialog.value = true
        else activity?.finish()
    }
    if (openDialog.value) {
        MaterialTheme {
            CustomDialog(setShowDialog = {
                openDialog.value = false
            }, noBtn = {
                openDialog.value = false
                activity?.finish()
            }, yesBtn = {
                openDialog.value = false
                drawController.saveBitmap()
            })
        }
    }
}