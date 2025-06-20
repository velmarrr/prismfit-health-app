package com.example.prismfit.notes.presentation.add_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prismfit.R
import com.example.prismfit.core.ui.utils.UiText
import com.example.prismfit.notes.presentation.add_note.model.NoteInput
import com.example.prismfit.notes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    private val _networkErrorMessage = MutableStateFlow<UiText?>(null)
    val networkErrorMessage: StateFlow<UiText?> = _networkErrorMessage

    private var noteId: String? = null

    fun initWithId(id: String?) {
        if (id == noteId) return
        noteId = id
        if (id != null) {
            viewModelScope.launch {
                try {
                    val note = noteRepository.getNotes().find { it.id == id }
                    note?.let {
                        _state.value = ScreenState(
                            inputTitle = it.title,
                            inputContent = it.content
                        )
                    }
                } catch (e: Exception) {
                    _networkErrorMessage.value = UiText.StringResource(R.string.network_error)
                }
            }
        }
    }

    fun save() {
        val title = _state.value.inputTitle.trim()
        val content = _state.value.inputContent.trim()
        if (title.isEmpty()) {
            _state.update {
                it.copy(errorMessage = UiText.StringResource(R.string.title_requirement))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                noteRepository.saveNote(
                    NoteInput(
                        id = noteId,
                        title = title,
                        content = content
                    )
                )
                _exitChannel.send(Unit)
            } catch (e: Exception) {
                _networkErrorMessage.value = UiText.StringResource(R.string.save_error)
            } finally {
                _state.update { it.copy(isSaving = false) }
            }
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

    fun dismissNetworkError() {
        _networkErrorMessage.value = null
    }

    data class ScreenState(
        val inputTitle: String = "",
        val inputContent: String = "",
        val isSaving: Boolean = false,
        val errorMessage: UiText? = null
    )
}
