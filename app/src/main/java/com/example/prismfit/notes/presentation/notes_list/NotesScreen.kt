package com.example.prismfit.notes.presentation.notes_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle

@Composable
fun NotesScreen(onNoteClick: (String) -> Unit) {

    val viewModel: NotesViewModel = hiltViewModel()
    val notes by viewModel.notesFlow.collectAsStateWithLifecycle()
    val noteToDelete by viewModel.noteToDelete.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getNotes()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            NotesContent(
                notes = notes,
                noteToDelete = noteToDelete,
                onAction = { action ->
                    when (action) {
                        is NotesAction.DeleteRequest -> viewModel.requestDeleteNote(action.noteId)
                        NotesAction.DeleteConfirm -> viewModel.confirmDelete()
                        NotesAction.DeleteCancel -> viewModel.cancelDelete()
                        is NotesAction.NoteClick -> onNoteClick(action.noteId)
                    }
                },
                formatDate = viewModel::formatDate
            )
        }
    }
}
