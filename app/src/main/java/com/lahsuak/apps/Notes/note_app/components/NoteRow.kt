package com.example.note_app.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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




@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    noteViewModel: NoteViewModel? = null,
    onNoteClick: (Note) -> Unit,
    onDeleteNote: () -> Unit,
    onEditNote: () -> Unit,
    onLockNote: (Boolean, String?) -> Unit
) {
    val context = LocalContext.current
    var isLocked by rememberSaveable { mutableStateOf(note.isLocked) }
    var password by rememberSaveable { mutableStateOf(note.password ?: "") }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var inputPassword by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(true) }
    var isExpanded by remember { mutableStateOf(false) }

    val textSize = remember { mutableStateOf(note.textSize.sp) }
    val isBold = remember { mutableStateOf(note.isBold) }
    val backgroundColor = remember { mutableStateOf(Color(note.backgroundColor)) }
    val selectedFont = remember { mutableStateOf(note.selectedFont) }

    // ✅ Default Light Blue Color
    val defaultLightBlue = Color(0xFF59c0e7)

// ✅ Ensure Card Background is Correct
    val appliedBackgroundColor = when {
        backgroundColor.value == Color.Unspecified -> defaultLightBlue // ✅ Default Light Blue
        else -> backgroundColor.value // ✅ Custom Color Selected
    }


    // ✅ Function to Get Correct FontFamily Based on Selection
    fun getFontFamily(font: String): FontFamily {
        return when (font) {
            "Serif" -> FontFamily.Serif
            "Sans-serif" -> FontFamily.SansSerif
            "Monospace" -> FontFamily.Monospace
            "Cursive" -> FontFamily.Cursive
            "Fantasy" -> FontFamily.Default
            "Roboto" -> FontFamily.Default
            "Lobster" -> FontFamily.Default
            "Dancing Script" -> FontFamily.Default
            "Playfair Display" -> FontFamily.Default
            "Poppins" -> FontFamily.Default
            "Raleway" -> FontFamily.Default
            "Open Sans" -> FontFamily.Default
            "Nunito" -> FontFamily.Default
            "Oswald" -> FontFamily.Default
            else -> FontFamily.Default
        }
    }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            when (it) {
                DismissValue.DismissedToEnd -> {
                    showPasswordDialog = true
                    false
                }

                DismissValue.DismissedToStart -> {
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
                        .padding(top = 12.dp)
                        .then(
                            if (!isLocked) Modifier.clickable { isExpanded = !isExpanded }
                            else Modifier
                        ),
                    shape = RoundedCornerShape(12.dp), // ✅ Smooth Rounded Corners
                    elevation = 2.dp, // ✅ Minimal Shadow (Avoid Extra Layer Look)
                    backgroundColor = appliedBackgroundColor // ✅ Background Color Applied Properly
                ) {
                    Column(modifier = Modifier.padding(12.dp)) { // ✅ Inner Padding Added
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = note.title,
                                    fontSize = textSize.value,
                                    fontWeight = if (isBold.value) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = getFontFamily(selectedFont.value),
                                    color = if (isLocked) Color.Gray else Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = note.timeStamp,
                                    fontSize = 12.sp,
                                    fontFamily = getFontFamily(selectedFont.value),
                                    color = Color.Gray
                                )
                            }

                            Icon(
                                imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = "Lock Note",
                                tint = if (isLocked) Color.Red else Color.Gray
                            )
                        }


                        AnimatedVisibility(visible = isExpanded) {
                            Column {
                                Text(
                                    text = note.note,
                                    fontSize = textSize.value,
                                    fontWeight = if (isBold.value) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = getFontFamily(selectedFont.value),
                                    color = Color.Black
                                )
                                if (!note.imageList.isNullOrEmpty()) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                                    ) {
                                        ImageSliderBitmap(imageList = note.imageList)


                                        IconButton(
                                            onClick = { onEditNote() },
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(8.dp)
                                                .size(20.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Note",
                                                tint = Color.Black
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }


            }
        )
    }


// ✅ Lock/Unlock Password Dialog (Beautiful UI)
    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            shape = RoundedCornerShape(16.dp), // ✅ Smooth Rounded Corners
            backgroundColor =  androidx.compose.material3.MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = if (isLocked) "🔒 Enter Password to Unlock" else "🔑 Set Password to Lock",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Column {
                    var passwordVisible by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = inputPassword,
                        onValueChange = { inputPassword = it },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Blue,
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isLocked) {
                            if (inputPassword == password) {
                                isLocked = false
                                onLockNote(false, null)
                                showPasswordDialog = false
                            } else {
                                Toast.makeText(context, " Wrong Password", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            password = inputPassword
                            isLocked = true
                            onLockNote(true, password)
                            showPasswordDialog = false
                        }
                        inputPassword = ""
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface)
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showPasswordDialog = false },
                    border = BorderStroke(1.dp, Color.Blue)
                ) {
                    Text("Cancel", color = Color.Red)
                }
            }
        )
    }

// ✅ Smooth Delete Animation
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
        dismissState.dismissDirection == DismissDirection.EndToStart -> Color.Red // ✅ Left Swipe → Delete
        dismissState.dismissDirection == DismissDirection.StartToEnd -> Color(0xFF039BE5) // ✅ Right Swipe → Lock
        else -> Color.Transparent
    }

    val alignment = when (dismissState.dismissDirection) {
        DismissDirection.EndToStart -> Alignment.CenterEnd
        DismissDirection.StartToEnd -> Alignment.CenterStart
        else -> Alignment.Center
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp)) // ✅ Rounded Corners Added
            .background(color)
            .padding(12.dp),
        contentAlignment = alignment
    ) {
        when {
            isLocked -> { // ✅ Locked Notes Can't Be Edited or Deleted
                Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.White)
            }
            dismissState.dismissDirection == DismissDirection.EndToStart -> { // ✅ Delete Option
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
            dismissState.dismissDirection == DismissDirection.StartToEnd -> { // ✅ Lock Option
                Icon(Icons.Default.Lock, contentDescription = "Lock Note", tint = Color.White)
            }
        }
    }
}