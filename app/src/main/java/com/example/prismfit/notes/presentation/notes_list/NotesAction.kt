package com.example.prismfit.notes.presentation.notes_list

sealed interface NotesAction {
    data class DeleteRequest(val noteId: String) : NotesAction
    data object DeleteConfirm : NotesAction
    data object DeleteCancel : NotesAction
    data class NoteClick(val noteId: String) : NotesAction
}
