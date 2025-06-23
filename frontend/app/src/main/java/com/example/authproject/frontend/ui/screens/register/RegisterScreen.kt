package com.example.authproject.frontend.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.authproject.frontend.util.Resource
import com.example.authproject.frontend.util.isPasswordValid

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isFormValid by remember {
        derivedStateOf { email.isNotBlank() && isPasswordValid(password) }
    }
    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        if (registerState is Resource.Success) {
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Register", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("Min. 9 chars, with uppercase, lowercase, and a digit.")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.register(email, password) },
                enabled = isFormValid && registerState !is Resource.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }

            registerState?.let {
                when (it) {
                    is Resource.Loading -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }
                    is Resource.Error -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = it.message ?: "An error occurred", color = MaterialTheme.colorScheme.error)
                    }
                    else -> {}
                }
            }
        }
    }
} 