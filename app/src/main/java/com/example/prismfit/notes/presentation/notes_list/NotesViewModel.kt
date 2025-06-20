package com.example.prismfit.notes.presentation.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.R
import com.example.prismfit.core.network.NetworkMonitor
import com.example.prismfit.core.ui.utils.UiText
import com.example.prismfit.notes.domain.model.Note
import com.example.prismfit.notes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _notesFlow = MutableStateFlow<List<Note>>(emptyList())
    val notesFlow: StateFlow<List<Note>> = _notesFlow

    private val _noteToDelete = MutableStateFlow<String?>(null)
    val noteToDelete: StateFlow<String?> = _noteToDelete

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _networkErrorMessage = MutableStateFlow<UiText?>(null)
    val networkErrorMessage: StateFlow<UiText?> = _networkErrorMessage

    val isConnected: StateFlow<Boolean> = networkMonitor.isConnected

    fun getNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _notesFlow.value = noteRepository.getNotes()
                    .sortedByDescending { it.createdAt }
            } catch (e: Exception) {
                _networkErrorMessage.value = UiText.StringResource(R.string.network_error)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun requestDeleteNote(id: String) {
        _noteToDelete.value = id
    }

    fun confirmDelete() {
        val id = _noteToDelete.value ?: return
        viewModelScope.launch {
            try {
                noteRepository.deleteNote(id)
                _noteToDelete.value = null
                getNotes()
            } catch (e: Exception) {
                _networkErrorMessage.value = UiText.StringResource(R.string.deletion_error)
            }
        }
    }

    fun cancelDelete() {
        _noteToDelete.value = null
    }

    fun dismissNetworkError() {
        _networkErrorMessage.value = null
    }

    fun formatDate(ms: Long?): String {
        return ms?.let {
            val sdf = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
            sdf.format(Date(it))
        } ?: ""
    }

    companion object {
        private const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm"
    }
}