package com.lahsuak.apps.Notes.note_app// package com.example.note_app
//
//import android.os.Build
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.viewModels
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.note_app.navigation.NoteScreenNavigation
//import com.example.note_app.viewModel.NoteViewModel
//import dagger.hilt.android.AndroidEntryPoint
//
//
//
//
// @AndroidEntryPoint
//
//class NotesActivity : ComponentActivity() {
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//
//                val noteViewModel:NoteViewModel by viewModels()
//                // A surface container using the 'background' color from the theme
//                NoteScreenNavigation(this,noteViewModel)
//            }
//        }
//    }
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//
//}