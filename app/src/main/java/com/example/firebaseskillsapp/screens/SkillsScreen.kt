package com.example.firebaseskillsapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebaseskillsapp.viewmodel.SkillsViewModel
import com.example.firebaseskillsapp.viewmodel.SkillsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(
    viewModel: SkillsViewModel = viewModel(),
    onLogout: () -> Unit = {}
) {
    var skillInput by remember { mutableStateOf("") }
    val skillsState by viewModel.skillsState.collectAsState()
    val skills by viewModel.skills.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Skills") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    TextButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Text("Logout", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = skillInput,
                onValueChange = { skillInput = it },
                label = { Text("Enter a skill") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (skillInput.isNotBlank()) {
                        viewModel.addSkill(skillInput)
                        skillInput = ""
                    } else {
                        viewModel.setError("Skill cannot be empty")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Skill")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status messages
            when (skillsState) {
                is SkillsState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is SkillsState.Error -> {
                    Text(
                        text = (skillsState as SkillsState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                is SkillsState.Success -> {
                    Text(
                        text = (skillsState as SkillsState.Success).message,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Skills:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // List of skills
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(skills) { skill ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = skill.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            IconButton(
                                onClick = { viewModel.deleteSkill(skill.id) }
                            ) {
                                Text("✕", style = MaterialTheme.typography.titleLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}