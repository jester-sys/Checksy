package com.lahsuak.apps.Notes.note_app.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSliderBitmap(imageList:List<String>){

    if (imageList.isNotEmpty()) {


        val pagerState = rememberPagerState { imageList.size }

        Box(
            modifier =
            Modifier

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
                HorizontalPager(state = pagerState) { page ->
                    val path: String = imageList.reversed()[page]
                    var imgBitmap: Bitmap? = null

                    if (path.isNotEmpty()) {
                        val imgFile = File(path)
                        if (imgFile.exists()) {
                            imgBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                        }
                    }

                    Image(
                        painter = rememberAsyncImagePainter(imgBitmap),
                        contentDescription = null
                    )
                }
            }

        }

    }
}