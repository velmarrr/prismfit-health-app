package com.example.prismfit.notes.presentation.add_note

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prismfit.navigation.LocalNavController
import com.example.prismfit.notes.presentation.EventConsumer

@Composable
fun AddNoteScreen(noteId: String?) {

    val viewModel: AddNoteViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val networkErrorMessage by viewModel.networkErrorMessage.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    EventConsumer(viewModel.exitChannel) {
        navController.popBackStack()
    }

    LaunchedEffect(networkErrorMessage) {
        networkErrorMessage?.let {
            Toast.makeText(context, it.asString(context), Toast.LENGTH_LONG).show()
            viewModel.dismissNetworkError()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message.asString(context))
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(noteId) {
        viewModel.initWithId(noteId)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AddNoteContent(
            state = state,
            onTitleChange = viewModel::onTitleChange,
            onContentChange = viewModel::onContentChange,
            onSave = viewModel::save
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
