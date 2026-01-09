package com.example.firebaseskillsapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebaseskillsapp.viewmodel.RegisterState
import com.example.firebaseskillsapp.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,
    onNavigateToLogin: () -> Unit, // Callback to go back to Login
    viewModel: RegisterViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val registerState by viewModel.registerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Box to constrain width (same as LoginScreen)
        Box(modifier = Modifier.fillMaxWidth(0.85f)) {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Validation logic matches your LoginScreen style
                        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            viewModel.setError("All fields are required")
                        } else if (password != confirmPassword) {
                            viewModel.setError("Passwords do not match")
                        } else {
                            viewModel.register(email, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }

                // Link to return to Login
                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Already have an account? Login")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Centered feedback area
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when (registerState) {
                        is RegisterState.Loading -> CircularProgressIndicator()
                        is RegisterState.Success -> {
                            LaunchedEffect(Unit) {
                                onRegisterSuccess((registerState as RegisterState.Success).email)
                            }
                        }
                        is RegisterState.Error -> Text(
                            text = (registerState as RegisterState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        else -> {} // Idle
                    }
                }
            }
        }
    }
}