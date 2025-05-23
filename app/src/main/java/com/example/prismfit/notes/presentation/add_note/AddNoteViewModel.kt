package com.example.prismfit.notes.presentation.add_note

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.R
import com.example.prismfit.notes.data.model.NoteRequest
import com.example.prismfit.notes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    private var noteId: String? = null

    fun initWithId(id: String?) {
        if (id == noteId) return
        noteId = id
        if (id != null) {
            viewModelScope.launch {
                val note = noteRepository.getNotes().find { it.id == id }
                note?.let {
                    _state.value = ScreenState(
                        inputTitle = it.title,
                        inputContent = it.content
                    )
                }
            }
        }
    }

    fun save() {
        val title = _state.value.inputTitle.trim()
        if (title.isEmpty()) {
            _state.update { it.copy(errorMessage = context.getString(R.string.title_requirement)) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }
            noteRepository.saveNote(
                NoteRequest(
                    id = noteId,
                    title = title,
                    content = _state.value.inputContent.trim()
                )
            )
            _exitChannel.send(Unit)
        }
    }

    fun onTitleChange(newTitle: String) {
        val limit = newTitle.take(70)
        _state.update { it.copy(inputTitle = limit) }
    }

    fun onContentChange(newContent: String) {
        val limit = newContent.take(1000)
        _state.update { it.copy(inputContent = limit) }
    }

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
    }

    data class ScreenState(
        val inputTitle: String = "",
        val inputContent: String = "",
        val isSaving: Boolean = false,
        val errorMessage: String? = null
    )
}
