package com.example.note_app.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_app.model.Note
import com.example.note_app.repository.NoteRepository
import com.jaixlabs.checksy.model.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(val repository: NoteRepository) : ViewModel() {
    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()

    // ✅ Add Search Query Flow
    val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllNotes()
                .distinctUntilChanged()
                .collect { notes ->
                    viewModelScope.launch(Dispatchers.Main) {
                        _noteList.value = notes
                    }
                }
        }
    }

    // ✅ Sorting Notes
    fun onSortOrderSelected(sortOrder: SortOrder, context: Context) = viewModelScope.launch {
        val sortedList = when (sortOrder) {
            SortOrder.BY_DATE -> _noteList.value.sortedByDescending { it.timeStamp }
            SortOrder.BY_DATE_DESC -> _noteList.value.sortedBy { it.timeStamp }
            SortOrder.BY_NAME -> _noteList.value.sortedBy { it.title.lowercase() }
            SortOrder.BY_NAME_DESC -> _noteList.value.sortedByDescending { it.title.lowercase() }
            SortOrder.BY_CATEGORY -> _noteList.value.sortedBy { it.category ?: "" }
            SortOrder.BY_CATEGORY_DESC -> _noteList.value.sortedByDescending { it.category ?: "" }
        }
        _noteList.value = sortedList
    }

    // ✅ Search Query Update
    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    // ✅ View Type Change (List/Grid)
    fun onViewTypeChanged(isListView: Boolean, context: Context) {
        Log.d("NoteViewModel", "View Type Changed: ${if (isListView) "List View" else "Grid View"}")
    }

    // ✅ Ensure Database Insert Runs on IO Thread
    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.addNote(note)
    }


    // ✅ Deleting a Note
    fun deleteNote(note_id: String) = viewModelScope.launch {
        repository.deleteNote(note_id)
    }

    // ✅ Update Lock Status (Locked/Unlocked with Password)
    fun updateNoteLockStatus(noteId: String, isLocked: Boolean, password: String?) = viewModelScope.launch {
        repository.updateNoteLockStatus(noteId, isLocked, password)
    }
}


//@HiltViewModel
//class NoteViewModel @Inject constructor(val repository: NoteRepository) :ViewModel() {
//    private val _noteList = MutableStateFlow<List<Note>>(emptyList<Note>())
//    val noteList=_noteList.asStateFlow()
//    fun size():Int{
//        return noteList.value.size
//    }
//
//
//
//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getAllNotes().distinctUntilChanged().collect(){
//                if(it!=null){
//                    _noteList.value=it
//                }
//
//
//            }
//        }
//    }
//
//    fun addNote(note:Note)=viewModelScope.launch {
//        repository.addNote(note )
//    }
//
////    fun updateNote(note:Note)=viewModelScope.launch {
////        repository.updateNote(note )
////    }
//    fun deleteNote(note_id:String)=viewModelScope.launch {
//        repository.deleteNote(note_id)
//    }
//
////    fun deleteNote(note:Note)=viewModelScope.launch {
////        repository.deleteNote(note)
////    }
//}