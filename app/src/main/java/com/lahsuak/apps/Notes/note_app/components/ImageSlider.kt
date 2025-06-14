package com.lahsuak.apps.Notes.note_app.components

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun ImageSlider(imageList: List<Uri>) {

    val scale = remember { mutableStateOf(1f) }
    val rotationState = remember { mutableStateOf(1f) }

    if (imageList.isNotEmpty()) {


        val pagerState = rememberPagerState { imageList.size }

        Box(
            modifier =
            Modifier.pointerInput(Unit){
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    scale.value *= zoom
                    rotationState.value += rotation
                }
            }
                .fillMaxWidth()
                .height(300.dp)
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                elevation = 10.dp,
                border = BorderStroke(5.dp, Color.Gray)
            ) {
                HorizontalPager(
                    state = pagerState
                ) { page ->
                    Image(
                        modifier = Modifier.graphicsLayer(
                            scaleX = maxOf(.5f, minOf(3f, scale.value)),
                            scaleY = maxOf(.5f, minOf(3f, scale.value)),
                            rotationZ = rotationState.value
                        ),
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(imageList[page])
                                .apply {
                                    scale(Scale.FILL)
                                    crossfade(true)
                                }
                                .build()
                        ),
                        contentDescription = null
                    )
                }

            }

        }

    }
}