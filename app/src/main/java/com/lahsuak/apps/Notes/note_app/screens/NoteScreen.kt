package com.lahsuak.apps.Notes.note_app.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lahsuak.apps.Notes.note_app.components.ImageSlider
import com.example.note_app.model.Note
import com.example.note_app.viewModel.NoteViewModel
import com.jaixlabs.checksy.ui.navigation.NavigationItem
import com.jaixlabs.checksy.ui.screens.TaskStatus
import com.jaixlabs.checksy.util.preference.SettingPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteScreen(
    navController: NavController? = null,
    noteViewModel: NoteViewModel? = null,
    note_obj: Note? = null,

    ) {

    val context = LocalContext.current


    //image from gallery

    val galleryImage = remember {
        mutableStateListOf<String>(emptyList<String>().toString())
    }

    val listAlert = remember {
        mutableStateOf(false)
    }
    val title = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val isLocked = remember { mutableStateOf(false) }
    val note = remember {
        mutableStateOf("")
    }

    val isBold = remember { mutableStateOf(false) }

    val textSize = remember { mutableStateOf(note_obj?.getTextSize() ?: 16.sp) }

    // ✅ Default Light Blue Color
    val defaultLightBlue = Color(0xFF59c0e7)


// ✅ System Theme Check
    val isDarkTheme = isSystemInDarkTheme()

// ✅ Ensure Proper Default Background
    val backgroundColor = remember {
        mutableStateOf(
            note_obj?.backgroundColor?.takeIf { it != Color.Unspecified.toArgb() }?.let { Color(it) }
                ?: Color.Unspecified
        )
    }


// ✅ Apply Background: Custom → System Theme → Default Light Blue
    val appliedBackgroundColor = when {
        backgroundColor.value != Color.Unspecified -> backgroundColor.value // ✅ User selected color
        isDarkTheme -> androidx.compose.material3.MaterialTheme.colorScheme.surface
        else -> androidx.compose.material3.MaterialTheme.colorScheme.surface
    }





    val selectedFont = remember { mutableStateOf("Default") }

    var showThemePicker by remember { mutableStateOf(false) }
    var showFontPicker by remember { mutableStateOf(false) }
    var showFontSizePicker by remember { mutableStateOf(false) }


    var isTaskDone by rememberSaveable {
        mutableStateOf(TaskStatus.isTaskDone) // ✅ Global state sync
    }

    var isNotes by rememberSaveable {
        mutableStateOf(TaskStatus.isNotes) // ✅ Global state sync
    }




// ✅ Choose text & icon colors dynamically
    val textColor = when {
        backgroundColor.value == Color.Unspecified -> if (isDarkTheme) Color.White else Color.Black
        else -> Color.Black // ✅ Custom theme → Always black
    }

    val iconColor = textColor // ✅ Icons ka color bhi text ke saath match karega


// ✅ Text & Icon Color: White in Dark Mode, Black otherwise
    val appliedTextColor = when {
        backgroundColor.value == Color.Unspecified -> if (isDarkTheme) Color.White else Color.Black
        else -> Color.Black // ✅ Custom theme → Always black
    }



    var id: String = UUID
        .randomUUID()
        .toString()

    var isEditing by remember { mutableStateOf(false) }
    if (note_obj != null) {
        title.value = note_obj.title
        note.value = note_obj.note
        id = note_obj.id
        isEditing = true

        listAlert.value = true
    }


    val formatted = remember {
        mutableStateOf("")
    }
    val timer = remember {
        mutableStateOf(true)
    }
    var uriImage = remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = {
            uriImage.value = it
        })


    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = timer.value) {
        while (timer.value) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            formatted.value = current.format(formatter).toString()

            delay(100L)
        }
    }


        BottomSheetScaffold(
            backgroundColor = appliedBackgroundColor,
            sheetContent = {
                when {
                    showThemePicker -> ThemePicker(backgroundColor) { showThemePicker = false }
                    showFontPicker -> FontPicker(selectedFont) { showFontPicker = false }
                    showFontSizePicker -> FontSizePicker(textSize) { showFontSizePicker = false }
                }
            },
            sheetPeekHeight = 0.dp,
            scaffoldState = rememberBottomSheetScaffoldState()
        ) {


            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = appliedBackgroundColor
            ) {


                Scaffold(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(appliedBackgroundColor),
                    topBar = {

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(appliedBackgroundColor)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, end = 12.dp, start = 8.dp)
                                    .background( appliedBackgroundColor)

                            ) {
                                // 🔙 Back Button
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = iconColor,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable {
                                            navController?.popBackStack()
                                            navController?.navigate(NavigationItem.Task.route)
                                        }
                                )

                                // 🎨 Theme, Font, Size & Image Icons
                                Row(
                                ) {
                                    Icon(Icons.Default.Palette, contentDescription = "Theme",
                                        tint = iconColor,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { showThemePicker = true }
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))

                                    Icon(Icons.Default.FontDownload, contentDescription = "Font",
                                        tint = iconColor,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { showFontPicker = true }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))

                                    Icon(Icons.Default.FormatSize, contentDescription = "Font Size",
                                        tint = iconColor,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { showFontSizePicker = true }
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))

                                    // 📷 Upload Image
                                    Icon(Icons.Default.UploadFile,
                                        contentDescription = "Upload Image",
                                        tint = iconColor,
                                        modifier = Modifier
                                            .background(
                                                if (backgroundColor.value == Color.Unspecified)
                                                    androidx.compose.material3.MaterialTheme.colorScheme.surface
                                                else
                                                    backgroundColor.value // ✅ User Selected Color
                                            )
                                            .size(24.dp)
                                            .clickable { launcher.launch("image/*") }
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))

                                    // ✅ Save Note
                                    Icon(Icons.Rounded.Done, contentDescription = "Save Note",
                                        tint = iconColor,
                                        modifier = Modifier

                                            .size(24.dp)
                                            .clickable {
                                                coroutineScope.launch(Dispatchers.IO) {
                                                    try {
                                                        val newGalleryImage = mutableListOf<String>()

                                                        coroutineScope {
                                                            val imageSaveJobs = uriImage.value.map { uri ->
                                                                async(Dispatchers.IO) {
                                                                    val inputStream = context.contentResolver?.openInputStream(uri)
                                                                    val bitmap = BitmapFactory.decodeStream(inputStream)
                                                                    inputStream?.close()

                                                                    bitmap?.let {
                                                                        val stream = ByteArrayOutputStream()
                                                                        it.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                                                                        val imageData: ByteArray = stream.toByteArray()

                                                                        val fileName = "image_${System.currentTimeMillis()}.jpg"
                                                                        val imageFile = File(context.filesDir, fileName)
                                                                        FileOutputStream(imageFile).use { it.write(imageData) }

                                                                        newGalleryImage.add(imageFile.absolutePath)
                                                                    }
                                                                }
                                                            }

                                                            imageSaveJobs.awaitAll() // ✅ Ensure all images are saved
                                                        }

                                                        Log.d("NoteScreen", "🟢 Images Saved, Now Saving Note")

                                                        noteViewModel?.addNote(
                                                            Note(
                                                                id = id,
                                                                title = title.value,
                                                                note = note.value,
                                                                imageList = newGalleryImage,
                                                                timeStamp = formatted.value,
                                                                isLocked = isLocked.value,
                                                                password = password.value,
                                                                isBold = isBold.value,
                                                                textSize = textSize.value.value,
                                                                backgroundColor = if (backgroundColor.value == Color.Unspecified) defaultLightBlue.toArgb() else backgroundColor.value.toArgb(),
                                                                selectedFont = selectedFont.value
                                                            )
                                                        )

                                                        Log.d("NoteScreen", "✅ Note Saved Successfully")

                                                        withContext(Dispatchers.Main) {
                                                            isNotes = true  // ✅ Ensure "Notes" status is selected when navigating back
                                                            isTaskDone = false

                                                            navController?.navigate(NavigationItem.Task.route)
                                                        }

                                                    } catch (e: Exception) {
                                                        Log.e("NoteScreen", "❌ Error processing image: ${e.message}")
                                                    }
                                                }


                                            }
                                    )
                                }
                            }
                        }
                    }

                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(appliedBackgroundColor)
                            .verticalScroll(rememberScrollState())
                            .padding(it)
                    ) {
                        TextField(
                            value = title.value,
                            onValueChange = { title.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(appliedBackgroundColor), // ✅ Dynamic Background Color
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = when (selectedFont.value) { // ✅ Apply selected font to Title
                                    "Serif" -> FontFamily.Serif
                                    "Sans-serif" -> FontFamily.SansSerif
                                    "Monospace" -> FontFamily.Monospace
                                    "Cursive" -> FontFamily.Cursive
                                    "Fantasy" -> FontFamily.Default
                                    else -> FontFamily.Default
                                },
                                color = appliedTextColor // ✅ Dynamic Text Color
                            ),
                            placeholder = {
                                Text(
                                    text = "Title",
                                    fontFamily = when (selectedFont.value) { // ✅ Apply selected font to Placeholder
                                        "Serif" -> FontFamily.Serif
                                        "Sans-serif" -> FontFamily.SansSerif
                                        "Monospace" -> FontFamily.Monospace
                                        "Cursive" -> FontFamily.Cursive
                                        "Fantasy" -> FontFamily.Default
                                        else -> FontFamily.Default
                                    },
                                    style = MaterialTheme.typography.h4.copy(color = appliedTextColor) // ✅ Dynamic Placeholder Color
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = appliedBackgroundColor, // ✅ Dynamic Background Color
                                cursorColor = appliedTextColor, // ✅ Cursor color matches text
                                focusedIndicatorColor = Color.Transparent, // ✅ Hide Bottom Line
                                unfocusedIndicatorColor = Color.Transparent // ✅ Hide Bottom Line
                            ),
                        )




                        Text(
                            text = formatted.value,
                            style = MaterialTheme.typography.h6.copy(fontSize = 14.sp, color = appliedTextColor), // ✅ Dynamic text color applied
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, top = 4.dp, bottom = 8.dp)
                                .background(appliedBackgroundColor)
                                .padding(4.dp), // ✅ Thoda padding diya taki text chipke na
                            color = appliedTextColor // ✅ Text color fixed
                        )



                        Divider()
                        Spacer(
                            modifier = Modifier
                                .size(10.dp)
                                .background(backgroundColor.value)
                        )

                        // 🔹 Image Slider (Agar Image Hai Toh Dikhega)
                        if (uriImage.value.isNotEmpty()) {
                            ImageSlider(uriImage.value)
                            Divider()
                        }

                        Spacer(
                            modifier = Modifier
                                .size(10.dp)
                                .background(backgroundColor.value)
                        )

                        TextField(
                            value = note.value,
                            onValueChange = { note.value = it },
                            placeholder = {
                                Text(
                                    text = "Write your note here",
                                    fontWeight = FontWeight.Light,
                                    fontFamily = when (selectedFont.value) { // ✅ Apply Selected Font to Placeholder
                                        "Serif" -> FontFamily.Serif
                                        "Sans-serif" -> FontFamily.SansSerif
                                        "Monospace" -> FontFamily.Monospace
                                        "Cursive" -> FontFamily.Cursive
                                        "Fantasy" -> FontFamily.Default
                                        else -> FontFamily.Default
                                    },
                                    color = appliedTextColor
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = textSize.value,
                                fontWeight = if (isBold.value) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = when (selectedFont.value) { // ✅ Apply Selected Font to Text Input
                                    "Serif" -> FontFamily.Serif
                                    "Sans-serif" -> FontFamily.SansSerif
                                    "Monospace" -> FontFamily.Monospace
                                    "Cursive" -> FontFamily.Cursive
                                    "Fantasy" -> FontFamily.Default
                                    else -> FontFamily.Default
                                },
                                color = appliedTextColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(appliedBackgroundColor),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = appliedBackgroundColor,
                                cursorColor = appliedTextColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )



                        if (listAlert.value) {
                            Spacer(modifier = Modifier.size(20.dp))
                            Row(horizontalArrangement = Arrangement.Center) {
                                Spacer(modifier = Modifier.fillMaxSize(0.25f))
                                Text(
                                    text = "On Updating, image list is reassign",
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }



            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePicker(backgroundColor: MutableState<Color>, onDismiss: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme() // ✅ Check if dark theme is active
    val pickerBackgroundColor = if (isDarkTheme) Color.DarkGray else Color.White
    val pickerTextColor = if (isDarkTheme) Color.White else Color.Black
    val accentColor = if (isDarkTheme) Color(0xFF702CC4) else Color(0xFF6200EA) // ✅ Accent Color for Selection

    val lightThemes = listOf(
        Color(0xFFFFF8E1), // Light Yellow
        Color(0xFFFFEBEE), // Light Pink
        Color(0xFFE3F2FD), // Light Blue
        Color(0xFFE8F5E9), // Light Green
        Color(0xFFFFF3E0), // Light Orange
        Color(0xFFFFFDE7), // Light Cream
        Color(0xFFF3E5F5), // Light Purple
        Color(0xFFE0F7FA), // Light Cyan
        Color(0xFFFFE0B2), // Light Peach
        Color(0xFFD7CCC8)  // Light Gray
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp) // ✅ Smooth Rounded Corners
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(pickerBackgroundColor)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🎨 **Title with Underline**
            Text(
                "Select Theme",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = pickerTextColor,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .drawBehind {
                        val strokeWidth = 5f
                        drawLine(
                            color = accentColor,
                            start = Offset(0f, size.height + strokeWidth),
                            end = Offset(size.width, size.height + strokeWidth),
                            strokeWidth = strokeWidth
                        )
                    }
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(lightThemes.chunked(5)) { rowColors ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        rowColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(CircleShape) // ✅ Rounded Shape
                                    .background(color)
                                    .clickable {
                                        backgroundColor.value = color
                                        onDismiss() // ✅ Close BottomSheet on Theme Select
                                    }
                                    .border(2.dp, Color.Gray, CircleShape)
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontPicker(selectedFont: MutableState<String>, onDismiss: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val pickerBackgroundColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
    val pickerTextColor = if (isDarkTheme) Color.White else Color.Black
    val accentColor = if (isDarkTheme) Color(0xFF0020FF) else Color(0xFF6200EA)

    val fonts = listOf(
        "Default", "Serif", "Sans-serif", "Monospace",
        "Cursive", "Fantasy", "Roboto", "Lobster", "Dancing Script",
        "Playfair Display", "Poppins", "Raleway", "Open Sans", "Nunito", "Oswald"
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(pickerBackgroundColor)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Select Font",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = pickerTextColor,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .drawBehind {
                        val strokeWidth = 5f
                        drawLine(
                            color = accentColor,
                            start = Offset(0f, size.height + strokeWidth),
                            end = Offset(size.width, size.height + strokeWidth),
                            strokeWidth = strokeWidth
                        )
                    }
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(fonts) { font ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedFont.value == font) accentColor.copy(alpha = 0.3f) else Color.Transparent)
                            .clickable {
                                selectedFont.value = font  // ✅ Updating font correctly
                                onDismiss()  // ✅ Close BottomSheet after selection
                            }
                            .padding(vertical = 10.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = font,
                            fontSize = 18.sp,
                            fontWeight = if (selectedFont.value == font) FontWeight.Bold else FontWeight.Normal,
                            color = pickerTextColor,
                            fontFamily = when (font) {
                                "Serif" -> FontFamily.Serif
                                "Sans-serif" -> FontFamily.SansSerif
                                "Monospace" -> FontFamily.Monospace
                                "Cursive" -> FontFamily.Cursive
                                else -> FontFamily.Default
                            }
                        )
                    }
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSizePicker(textSize: MutableState<TextUnit>, onDismiss: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val pickerBackgroundColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color(0xFFF5F5F5) // ✅ Softer Colors
    val pickerTextColor = if (isDarkTheme) Color.White else Color.Black
    val accentColor = if (isDarkTheme) Color(0xFF0026FF) else Color(0xFF3F51B5) // ✅ Accent for Slider

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp) // ✅ Smooth Rounded Corners
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(pickerBackgroundColor)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🎨 **Title with Underline**
            Text(
                "Select Font Size",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = pickerTextColor,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .drawBehind {
                        val strokeWidth = 5f
                        drawLine(
                            color = accentColor,
                            start = Offset(0f, size.height + strokeWidth),
                            end = Offset(size.width, size.height + strokeWidth),
                            strokeWidth = strokeWidth
                        )
                    }
            )

            // 🔘 **Font Size Display with Cool Background**
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.2f)) // ✅ Subtle Highlight
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    "${textSize.value.value.toInt()} sp",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = pickerTextColor
                )
            }

            // 🎚 **Styled Slider**
            Slider(
                value = textSize.value.value,
                onValueChange = { textSize.value = it.sp },
                valueRange = 12f..32f,
                colors = SliderDefaults.colors(
                    thumbColor = accentColor,
                    activeTrackColor = accentColor,
                    inactiveTrackColor = accentColor.copy(alpha = 0.3f) // ✅ Smooth Gradient Effect
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}


//fun DoneClickableEvent(value: String, value1: String, value2: List<Uri>, toString: String) {
//    val noteViewModel= viewModel<NoteViewModel>()
//
//}

//fun DoneClickableEvent(value: String, value1: String, value2: List<Uri>, toString: String) {
//    val noteViewModel= viewModel<NoteViewModel>()
//
//}