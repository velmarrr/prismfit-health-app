package com.example.prismfit.notes.presentation.notes_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.prismfit.R
import com.example.prismfit.notes.data.model.Note

@Composable
fun NotesScreen(onNoteClick: (String) -> Unit) {

    val viewModel: NotesViewModel = hiltViewModel()
    val notes by viewModel.notesFlow.collectAsStateWithLifecycle()
    val noteToDelete by viewModel.noteToDelete.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getNotes()
        }
    }

    NotesContent(
        notes = notes,
        onDeleteRequest = viewModel::requestDeleteNote,
        onDeleteConfirm = viewModel::confirmDelete,
        onDeleteCancel = viewModel::cancelDelete,
        noteToDelete = noteToDelete,
        onNoteClick = onNoteClick,
        formatDate = viewModel::formatDate
    )
}

@Composable
fun NotesContent(
    notes: List<Note>,
    onDeleteRequest: (String) -> Unit,
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    noteToDelete: String?,
    onNoteClick: (String) -> Unit,
    formatDate: (Long?) -> String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(notes) { note ->
            ListItem(
                headlineContent = { Text(note.title) },
                supportingContent = { Text(formatDate(note.createdAt)) },
                trailingContent = {
                    IconButton(
                        onClick = {
                            onDeleteRequest(note.id)
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                },
                modifier = Modifier.clickable { onNoteClick(note.id) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    if (noteToDelete != null) {
        AlertDialog(
            onDismissRequest = onDeleteCancel,
            title = { Text(stringResource(R.string.delete_confirmation)) },
            text = { Text(stringResource(R.string.delete_confirmation_question)) },
            confirmButton = {
                TextButton(onClick = onDeleteConfirm) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = onDeleteCancel) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}