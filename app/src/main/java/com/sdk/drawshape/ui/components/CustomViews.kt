package com.sdk.drawshape.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.krottv.compose.sliders.DefaultThumb
import com.github.krottv.compose.sliders.DefaultTrack
import com.github.krottv.compose.sliders.SliderValueHorizontal
import com.sdk.drawshape.R
import com.sdk.drawshape.drawbox.DrawController

@Composable
fun ControlsBar(
    drawController: DrawController,
    onDownloadClick: () -> Unit,
    onColorClick: () -> Unit,
    onBgColorClick: () -> Unit,
    onSizeClick: () -> Unit,
    undoVisibility: MutableState<Boolean>,
    redoVisibility: MutableState<Boolean>,
    colorValue: MutableState<Color>,
    bgColorValue: MutableState<Color>,
//    sizeValue: MutableState<Int>,
    onBottomSwipe: () -> Unit,
) {
    var offset by remember { mutableStateOf(0f) }
    if (offset > 200) {
        onBottomSwipe()
        offset = 0f
    }
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.scrollable(orientation = Orientation.Vertical,
            state = rememberScrollableState { delta ->
                offset += delta
                delta
            })
    ) {
        MenuItems(R.drawable.ic_size, "stroke size", MaterialTheme.colors.primary) {
            onSizeClick()
        }
        MenuItems(
            R.drawable.ic_undo,
            "undo",
            if (undoVisibility.value) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
        ) {
            if (undoVisibility.value) drawController.unDo()
        }
        MenuItems(
            R.drawable.ic_redo,
            "redo",
            if (redoVisibility.value) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
        ) {
            if (redoVisibility.value) drawController.reDo()
        }
        MenuItems(
            R.drawable.ic_refresh,
            "reset",
            if (redoVisibility.value || undoVisibility.value) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
        ) {
            drawController.reset()
        }
        MenuItems(R.drawable.ic_color, "background color", bgColorValue.value, border = true) {
            onBgColorClick()
        }
        MenuItems(R.drawable.ic_color, "stroke color", colorValue.value, border = true) {
            onColorClick()
        }
        MenuItems(
            R.drawable.ic_download,
            "download",
            if (undoVisibility.value) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
        ) {
            if (undoVisibility.value) onDownloadClick()
        }
    }
}

@Composable
fun RowScope.MenuItems(
    @DrawableRes resId: Int,
    desc: String,
    colorTint: Color,
    border: Boolean = false,
    onClick: () -> Unit,
) {
    val modifier = Modifier.size(24.dp)
    IconButton(
        onClick = onClick, modifier = Modifier.weight(1f, true)
    ) {
        Icon(
            painterResource(id = resId),
            contentDescription = desc,
            tint = colorTint,
            modifier = if (border) modifier.border(
                0.5.dp, Color.Gray, shape = CircleShape
            ) else modifier
        )
    }
}

@Composable
fun CustomSeekbar(
    isVisible: Boolean,
    max: Int = 200,
    progress: Int = max,
    thumbColor: Color,
    onBottomSwipe: () -> Unit,
    onProgressChanged: (Int) -> Unit,
) {
    var offset by remember { mutableStateOf(0f) }
    if (offset > 200) {
        onBottomSwipe()
        offset = 0f
    }
    val density = LocalDensity.current
    AnimatedVisibility(visible = isVisible, enter = slideInVertically {
        with(density) { -40.dp.roundToPx() }
    } + expandVertically(
        expandFrom = Alignment.Top
    ) + fadeIn(
        initialAlpha = 0.3f
    ), exit = slideOutVertically() + shrinkVertically() + fadeOut()) {
        var sliderPosition by remember { mutableStateOf(0f) }
        LaunchedEffect(Unit) {
            sliderPosition = progress.toFloat()
        }
        val thumbSize = sliderPosition / 2
        Column(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollableState { delta ->
                        offset += delta
                        delta
                    }), verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Stroke Width", modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp))
            SliderValueHorizontal(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                },
                valueRange = 10f..max.toFloat(),
                onValueChangeFinished = { onProgressChanged(sliderPosition.toInt()) },
                thumb = {
                        modifier: Modifier,
                        offset: Dp,
                        interactionSource: MutableInteractionSource,
                        enabled: Boolean,
                        thumbSize: DpSize,
                    ->
                    DefaultThumb(
                        modifier,
                        offset,
                        interactionSource,
                        enabled,
                        thumbSize,
                        color = thumbColor,
                        scaleOnPress = 1.1f
                    )
                },
                thumbSizeInDp = DpSize(thumbSize.dp, thumbSize.dp),
                track = {
                        modifier: Modifier,
                        fraction: Float,
                        interactionSource: MutableInteractionSource,
                        tickFractions: List<Float>,
                        enabled: Boolean,
                    ->

                    DefaultTrack(
                        modifier,
                        fraction,
                        interactionSource,
                        tickFractions,
                        enabled,
                        height = 8.dp,
                        colorTrack = Color.Gray.copy(alpha = 0.6f),
                        colorProgress = MaterialTheme.colors.primary
                    )
                })
        }
    }
}