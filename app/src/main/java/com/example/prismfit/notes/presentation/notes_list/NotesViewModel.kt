package com.example.prismfit.notes.presentation.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.notes.data.model.Note
import com.example.prismfit.notes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _notesFlow = MutableStateFlow<List<Note>>(emptyList())
    val notesFlow: StateFlow<List<Note>> = _notesFlow

    private val _noteToDelete = MutableStateFlow<String?>(null)
    val noteToDelete: StateFlow<String?> = _noteToDelete

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getNotes()
    }

    fun getNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            _notesFlow.value = noteRepository.getNotes()
                .sortedByDescending { it.createdAt }
            _isLoading.value = false
        }
    }

    fun requestDeleteNote(id: String) {
        _noteToDelete.value = id
    }

    fun confirmDelete() {
        val id = _noteToDelete.value
        if (id != null) {
            viewModelScope.launch {
                noteRepository.deleteNote(id)
                _noteToDelete.value = null
                getNotes()
            }
        }
    }

    fun cancelDelete() {
        _noteToDelete.value = null
    }

    fun formatDate(ms: Long?): String {
        return ms?.let {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            sdf.format(Date(it))
        } ?: ""
    }
}