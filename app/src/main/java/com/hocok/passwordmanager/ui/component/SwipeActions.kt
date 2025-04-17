package com.hocok.passwordmanager.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SwipeActions(
    isClose: Boolean,
    actions: @Composable () -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
){
    var cardWith by remember { mutableFloatStateOf(0f) }
    val offset = remember { Animatable(initialValue = 0f) }
    val scope = rememberCoroutineScope()
    val roundedCorner = remember { Animatable(initialValue = 10f) }

    LaunchedEffect(key1 = isClose, cardWith) {
        if (isClose) {
            roundedCorner.animateTo(10f)
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
            .height(IntrinsicSize.Min)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.onSizeChanged {
                cardWith = it.width.toFloat() - 5
            }
        ) {
            actions()
        }
        Surface(
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(roundedCorner.value.dp))
                .offset{ IntOffset(offset.value.toInt(), 0) }
                .pointerInput(cardWith){
                    detectHorizontalDragGestures(
                        onHorizontalDrag = {_, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount).coerceIn(0f, cardWith)
                                val newRoundedCorner = (roundedCorner.value - dragAmount).coerceIn(0f, 10f)
                                offset.snapTo(newOffset)
                                roundedCorner.snapTo(newRoundedCorner)
                            }
                        },
                        onDragEnd = {
                            when{
                                offset.value >= cardWith / 2 -> {
                                    scope.launch {
                                        offset.animateTo(cardWith)
                                        onExpand()
                                    }
                                }
                                else -> {
                                    scope.launch {
                                        roundedCorner.animateTo(10f)
                                        offset.animateTo(0f)
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }

}

@Composable
fun ActionIcon(
    onClick: () -> Unit,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
){
    IconButton(
        onClick = onClick,
        modifier = modifier.background(backgroundColor).fillMaxHeight().width(60.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White
        )
    }
}