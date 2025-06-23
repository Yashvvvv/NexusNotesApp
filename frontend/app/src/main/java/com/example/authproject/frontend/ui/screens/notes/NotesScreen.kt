package com.example.authproject.frontend.ui.screens.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.authproject.frontend.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    onLogout: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val createNoteState by viewModel.createNoteState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(createNoteState) {
        if (createNoteState is Resource.Success) {
            showDialog = false
            title = ""
            content = ""
            // Optionally, show a snackbar for success
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") },
                actions = {
                    IconButton(onClick = { viewModel.onLogout(onLogout) }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text(text = state.error ?: "An unknown error occurred", color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.notes) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Note") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.createNote(title, content) },
                    enabled = title.isNotBlank() && content.isNotBlank() && createNoteState !is Resource.Loading
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
} 