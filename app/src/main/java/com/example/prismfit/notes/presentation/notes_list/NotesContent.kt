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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prismfit.R
import com.example.prismfit.notes.domain.model.Note

@Composable
fun NotesContent(
    notes: List<Note>,
    noteToDelete: String?,
    onAction: (NotesAction) -> Unit,
    isConnected: Boolean,
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
                            onAction(NotesAction.DeleteRequest(note.id))
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                },
                modifier = Modifier.clickable {
                    onAction(NotesAction.NoteClick(note.id))
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    if (noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { onAction(NotesAction.DeleteCancel) },
            title = { Text(stringResource(R.string.delete_confirmation)) },
            text = { Text(stringResource(R.string.note_delete_confirmation_question)) },
            confirmButton = {
                TextButton(
                    onClick = { onAction(NotesAction.DeleteConfirm) },
                    enabled = isConnected
                ) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(NotesAction.DeleteCancel) }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}