package com.example.note_app.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.note_app.model.Note
import com.example.note_app.viewModel.NoteViewModel
import com.jaixlabs.checksy.R
import com.lahsuak.apps.Notes.note_app.components.ImageSliderBitmap
import kotlinx.coroutines.delay
import java.io.File


//@Composable
//fun NoteRow(
//    note: Note,
//    context: Context,
//    onNoteClick: (Note) -> Unit,
//    noteViewModel: NoteViewModel? = null
//) {
//    val isDialog = remember {
//        mutableStateOf(false)
//    }
//
//    if (note != null) {
//        val expanded = remember {
//            mutableStateOf(false)
//        }
//        Surface {
//            Card(
//                Modifier
//                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
//                    .fillMaxWidth()
//                    .pointerInput(Unit) {
//                        detectTapGestures(onLongPress = {
//                            //noteViewModel?.deleteNote(note.id)
//                            isDialog.value = true
//                        },
//                            onTap = {
//                                expanded.value = !expanded.value
//                            })
//                    },
//                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
//                elevation = 8.dp
//            )
//            {
//                Column(
//                    Modifier
//                        .fillMaxSize()
//                        .background(Color.Blue)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Start
//                    ) {
//                        if (note.imageList != null) {
//
//
//                            if (note.imageList.size >= 2) {
//                                if (note.imageList[1] != "") {
//                                    Surface(
//                                        Modifier
//                                            .padding(10.dp)
//                                            .size(80.dp), elevation = 4.dp,
//                                        shape = CircleShape
//                                    ) {
//                                        val imgFile = File(note.imageList[1])
//
//                                        // on below line we are checking if the image file exist or not.
//                                        var imgBitmap: Bitmap? = null
//                                        if (imgFile.exists()) {
//                                            imgBitmap =
//                                                BitmapFactory.decodeFile(imgFile.absolutePath)
//                                        }
//                                        Image(
//                                            painter = rememberAsyncImagePainter(imgBitmap),
//                                            contentDescription = null
//                                        )
//
////                            val bitmap:Bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolverl,uri)
//                                    }
//                                }
//                            }
////
//
//                        }
//                        Column(Modifier.padding(4.dp)) {
//
//                            Text(
//                                text = note.title,
//                                style = MaterialTheme.typography.h6
//                            )
//                            Spacer(modifier = Modifier.size(10.dp))
//                            Text(
//                                text = "Time: ${note.timeStamp}",
//                                style = MaterialTheme.typography.caption
//                            )
//
//                        }
//                    }
//
//                    //row ended
//
//                    AnimatedVisibility(visible = expanded.value) {
//                        Column(Modifier.padding(4.dp)) {
//                           val paths= mutableListOf<String>(emptyList<String>().toString())
//                           note.imageList!!.forEach {
//                               if(it.length>10){
//                                   Log.i("path_image", it)
//                                   paths.add(it)
//                               }
//                           }
//                            if(paths.size>1){
//                                Log.i("here", "NoteRow: "+paths.size.toString())
//                                ImageSliderBitmap(imageList = paths)
//                            }
//
//                            Spacer(modifier = Modifier.size(5.dp))
//                            Divider()
//                            Text(
//                                text = "Description: ${note.note}", fontSize = 13.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//
//
//                        }
//                    }
//
//                    Row {
//                        Spacer(Modifier.fillMaxSize(0.90f))
//                        Icon(imageVector = if (expanded.value) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.ArrowDropDown,
//                            contentDescription = null,
//                            Modifier.clickable {
//
//
//                                //action on click
//                                expanded.value = !expanded.value
//                            })
//                    }
//                }
//
//            }
//            if (isDialog.value) {
//                Dialog(onDismissRequest = { isDialog.value = false }) {
//                    Card(
//                        elevation = 20.dp, modifier = Modifier
//                            .padding(4.dp)
//                            .height(240.dp)
//                            .width(180.dp)
//                    ) {
//                        Column {
//                            Row(
//                                Modifier
//                                    .height(80.dp)
//                                    .padding(start = 2.dp, end = 2.dp)
//                                    .fillMaxWidth()
//                                    .clickable {
//                                        onNoteClick.invoke(note)
//                                        isDialog.value = false
//                                    }) {
//                                Text(
//                                    text = "Update", style = MaterialTheme.typography.h4,
//                                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
//                                )
//                                Spacer(modifier = Modifier.fillMaxSize(0.60f))
//                                Image(
//                                    imageVector = Icons.Rounded.Update, contentDescription = null,
//                                    modifier = Modifier
//                                        .size(30.dp)
//                                        .align(alignment = Alignment.CenterVertically)
//                                )
//                            }
//                            Divider()
//                            Row(
//                                Modifier
//                                    .height(80.dp)
//                                    .padding(start = 2.dp, end = 2.dp)
//                                    .fillMaxWidth()
//                                    .clickable {
//                                        noteViewModel?.deleteNote(note_id = note.id)
//                                        isDialog.value = false
//                                    }) {
//                                Text(
//                                    text = "Delete", style = MaterialTheme.typography.h4,
//                                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
//                                )
//                                Spacer(modifier = Modifier.fillMaxSize(0.60f))
//                                Image(
//                                    imageVector = Icons.Rounded.Delete, contentDescription = null,
//                                    modifier = Modifier
//                                        .size(30.dp)
//                                        .align(alignment = Alignment.CenterVertically)
//                                )
//                            }
//                            Divider()
//                            Row(
//                                Modifier
//                                    .height(80.dp)
//                                    .padding(start = 2.dp, end = 2.dp)
//                                    .fillMaxWidth()
//                                    .clickable {
//                                        isDialog.value = false
//                                    }) {
//                                Text(
//                                    text = "Lock  ", style = MaterialTheme.typography.h4,
//                                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
//                                )
//                                Spacer(modifier = Modifier.fillMaxSize(0.60f))
//                                Image(
//                                    imageVector = Icons.Rounded.Lock, contentDescription = null,
//                                    modifier = Modifier
//                                        .size(30.dp)
//                                        .align(alignment = Alignment.CenterVertically)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    noteViewModel: NoteViewModel? = null,
    onNoteClick: (Note) -> Unit,
    onDeleteNote: () -> Unit,
    onEditNote: () -> Unit,
    onLockNote: (Boolean, String?) -> Unit // ✅ Now takes locked state + password
) {
    var context = LocalContext.current
    val isExpanded = rememberSaveable { mutableStateOf(false) }
    val isSelected = rememberSaveable { mutableStateOf(false) }
    var isLocked by rememberSaveable { mutableStateOf(note.isLocked) }
    var password by rememberSaveable { mutableStateOf(note.password ?: "") }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var inputPassword by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(true) }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            when {
                isLocked -> false // ✅ If locked, prevent swipe actions
                it == DismissValue.DismissedToEnd -> { // Left Swipe → Edit Note
                    if (!isLocked) onEditNote()
                    false
                }
                it == DismissValue.DismissedToStart -> { // Right Swipe → Delete Note
                    if (!isLocked) {
                        show = false
                        true
                    } else false
                }
                else -> false
            }
        }
    )

    AnimatedVisibility(
        visible = show,
        exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = { DismissBackground(dismissState, isLocked) },
            dismissContent = {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .combinedClickable(
                            onClick = {
                                if (isLocked) {
                                    showPasswordDialog = true // ✅ Ask Password on Click
                                } else {
                                    isExpanded.value = !isExpanded.value
                                }
                            },
                            onLongClick = {
                                if (!isLocked) {
                                    showPasswordDialog = true // ✅ Ask Password for Locking
                                }
                            }
                        )
                        .border(
                            if (isSelected.value) 2.dp else 0.dp,
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.background(MaterialTheme.colors.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!note.imageList.isNullOrEmpty()) {
                                val imgFile = File(note.imageList.firstOrNull() ?: "")
                                val imgBitmap =
                                    if (imgFile.exists()) BitmapFactory.decodeFile(imgFile.absolutePath) else null

                                imgBitmap?.let {
                                    Image(
                                        painter = rememberAsyncImagePainter(it),
                                        contentDescription = "Note Image",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.h6,
                                    color = if (isLocked) Color.Gray else Color.Black // ✅ Locked Note Gray Out
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = note.timeStamp,
                                    style = MaterialTheme.typography.body2.copy(fontSize = 12.sp),
                                    color = Color.Gray
                                )
                            }

                            // ✅ Lock Icon (Pin ke saath)
                            Icon(
                                imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = "Lock Note",
                                tint = if (isLocked) Color.Red else Color.Gray
                            )
                        }

                        // ✅ Hide Content if Locked
                        AnimatedVisibility(visible = isExpanded.value && !isLocked) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = note.note, style = MaterialTheme.typography.body1)
                                if (!note.imageList.isNullOrEmpty() && note.imageList.size > 1) {
                                    ImageSliderBitmap(imageList = note.imageList)
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    // ✅ Password Dialog for Locking / Unlocking Notes
    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text(if (isLocked) "Enter Password" else "Set Password") },
            text = {
                Column {
                    TextField(
                        value = inputPassword,
                        onValueChange = { inputPassword = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (isLocked) {
                        // ✅ Unlocking Logic
                        if (inputPassword == password) {
                            isLocked = false
                            onLockNote(false, null) // ✅ Unlock and remove password
                            showPasswordDialog = false
                        } else {
                            Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // ✅ Locking Logic
                        password = inputPassword
                        isLocked = true
                        onLockNote(true, password)
                        showPasswordDialog = false
                    }
                    inputPassword = ""
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showPasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(500)
            onDeleteNote()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DismissBackground(dismissState: DismissState, isLocked: Boolean) {
    val color = when {
        isLocked -> Color.DarkGray // ✅ If locked, disable swipe
        dismissState.dismissDirection == DismissDirection.StartToEnd -> Color(0xFF039BE5) // ✅ Edit (Left Swipe)
        dismissState.dismissDirection == DismissDirection.EndToStart -> Color.Red // ✅ Delete (Right Swipe)
        else -> Color.Transparent
    }

    val alignment = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp),
        contentAlignment = alignment
    ) {
        when {
            isLocked -> { // ✅ Locked Notes Can't Be Edited or Deleted
                Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.White)
            }
            dismissState.dismissDirection == DismissDirection.StartToEnd -> { // ✅ Edit Option
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit Note",
                    tint = Color.White
                )
            }
            dismissState.dismissDirection == DismissDirection.EndToStart -> { // ✅ Delete Icon
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        }
    }
}


