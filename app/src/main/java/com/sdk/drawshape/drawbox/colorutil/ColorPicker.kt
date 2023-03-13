package com.sdk.drawshape.drawbox.colorutil

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    isVisible: Boolean,
    rowElementsCount: Int = defaultRowElementsCount,
    showShades: Boolean = false,
    colorIntensityOut: Int = defaultColorIntensity,
    unSelectedSize: Dp = defaultUnSelectedSize,
    selectedSize: Dp = defaultSelectedSize,
    colors: List<List<Color>> = colorArray,
    defaultColorOut: Color = defaultSelectedColor,
    onBottomSwipe: () -> Unit,
    clickedColor: (Color) -> Unit,
) {
    val grayColor = if (isSystemInDarkTheme()) Color.White else Color.Gray
    val colorIntensity =
        if (colorIntensityOut in 10 downTo -1) defaultColorIntensity else colorIntensityOut
    val density = LocalDensity.current

    var defaultColor by remember {
        mutableStateOf(defaultColorOut)
    }
    var defaultRow by remember {
        mutableStateOf(colors[0])
    }
    var subColorsRowVisibility by remember {
        mutableStateOf(true)
    }

    ChangeVisibility(isVisible, density) {
        val parentList = colors.chunked(rowElementsCount)
        var offset by remember { mutableStateOf(0f) }
        if (offset > 60) {
            onBottomSwipe()
            offset = 0f
        }
        Column(modifier = Modifier
            .padding(16.dp, 0.dp)
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    offset += delta
                    delta
                }
            )) {

            if (showShades) {
                ChangeVisibility(
                    subColorsRowVisibility,
                    density
                ) {
                    if (!defaultRow.contains(defaultColor)) {
                        defaultRow = colors.first { it.contains(defaultColor) }
                    }
                    val parentListIn = defaultRow.chunked(rowElementsCount)

                    Column {

                        parentListIn.forEachIndexed { _, colorRow ->
                            SubColorRow(
                                rowElementsCount = rowElementsCount,
                                colorRow = colorRow,
                                defaultColor = defaultColor,
                                unSelectedSize = unSelectedSize,
                                selectedSize = selectedSize
                            ) {
                                defaultColor = it
                                clickedColor(it)
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .clip(CircleShape)
                                .background(grayColor)
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                        )
                    }

                }
            }
            parentList.forEachIndexed { _, colorRow ->
                ColorRow(
                    rowElementsCount = rowElementsCount,
                    colorRow = colorRow,
                    colorIntensity = colorIntensity,
                    defaultColor = defaultColor,
                    unSelectedSize = unSelectedSize,
                    selectedSize = selectedSize
                ) { colorRowIn, color ->
                    subColorsRowVisibility =
                        subColorsRowVisibility == false || defaultColor.value != color.value
                    defaultColor = color
                    defaultRow = colorRowIn
                    if (subColorsRowVisibility)
                        clickedColor(color)
                }
            }
        }
    }
}

private const val defaultColorIntensity = 5
private const val defaultRowElementsCount = 10
private val defaultUnSelectedSize = 26.dp
private val defaultSelectedSize = 36.dp

//Kept public to be used by host app to preSelected color
val defaultSelectedColor = colorArray[1][defaultColorIntensity]







