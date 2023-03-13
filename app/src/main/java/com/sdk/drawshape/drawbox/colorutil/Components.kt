package com.sdk.drawshape.drawbox.colorutil

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun ColorRow(
    rowElementsCount: Int,
    colorRow: List<List<Color>>,
    colorIntensity: Int,
    defaultColor: Color,
    unSelectedSize: Dp,
    selectedSize: Dp,
    clickedColor: (List<Color>, Color) -> Unit,
) {
    Row {
        repeat(rowElementsCount) { rowIndex ->
            if (colorRow.size - 1 < rowIndex) {
                Spacer(Modifier.weight(1f, true))
                return@repeat
            }
            val color = colorRow[rowIndex]
            ColorDots(
                color[colorIntensity], color.contains(defaultColor), unSelectedSize, selectedSize
            ) {
                clickedColor(color, it)
            }
        }
    }
}

@Composable
internal fun SubColorRow(
    rowElementsCount: Int,
    colorRow: List<Color>,
    defaultColor: Color,
    unSelectedSize: Dp,
    selectedSize: Dp,
    clickedColor: (Color) -> Unit,
) {
    Row {
        repeat(rowElementsCount) { rowIndex ->
            if (colorRow.size - 1 < rowIndex) {
                Spacer(Modifier.weight(1f, true))
                return@repeat
            }
            val color = colorRow[rowIndex]
            ColorDots(
                color,
                color == defaultColor,
                unSelectedSize,
                selectedSize,
                clickedColor = clickedColor
            )
        }
    }
}

@Composable
internal fun ChangeVisibility(
    isVisible: Boolean,
    density: Density,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(visible = isVisible, enter = slideInVertically {
        // Slide in from 40 dp from the top.
        with(density) {
            -40.dp.roundToPx()
        }
    } + expandVertically(
        // Expand from the top.
        expandFrom = Alignment.Top
    ) + fadeIn(
        // Fade in with the initial alpha of 0.3f.
        initialAlpha = 0.3f
    ), exit = slideOutVertically() + shrinkVertically(
        shrinkTowards = Alignment.Top
    ) + fadeOut(), content = content)
}

@Composable
internal fun RowScope.ColorDots(
    color: Color,
    selected: Boolean,
    unSelectedSize: Dp = 26.dp,
    selectedSize: Dp = 36.dp,
    dotDescription: String = stringResource(id = com.sdk.drawshape.R.string.color_dot),
    clickedColor: (Color) -> Unit,
) {
    val dbAnimateAsState: Dp by animateDpAsState(
        targetValue = if (selected) selectedSize else unSelectedSize
    )
    val grayColor = if (isSystemInDarkTheme()) Color.White else Color.LightGray
    Column(
        modifier = Modifier.weight(1f, true),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                clickedColor(color)
            },
        ) {
            Icon(
                painterResource(id = com.sdk.drawshape.R.drawable.ic_color),
                contentDescription = dotDescription,
                tint = color,
                modifier = Modifier
                    .size(dbAnimateAsState),
            )
        }
        AnimatedVisibility(selected) {
            Box(
                modifier = Modifier
                    .width(25.dp)
                    .height(2.dp)
                    .clip(CircleShape)
                    .background(grayColor),
            )
        }
    }
}