package com.example.fitapp.ui.theme.screens.exercisers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fitapp.data.ExerciserViewModel
import com.example.fitapp.models.Exerciser

@Composable
fun ExerciserListScreen(navController: NavController) {
    val exerciserViewModel: ExerciserViewModel = viewModel()
    val exercisers = exerciserViewModel.exerciser
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        exerciserViewModel.fetchRecords(context)
    }

    LazyColumn {
        items(exercisers) { exerciser ->
            ExerciserCard(
                exerciser = exerciser,
                onDelete = { id -> exerciserViewModel.deleteRecord(id, context) },
                navController = navController
            )
        }
    }
}

@Composable
fun ExerciserCard(
    exerciser: Exerciser,
    onDelete: (String) -> Unit,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this exerciser?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    exerciser.id?.let { onDelete(it) }
                }) {
                    Text("Yes", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            exerciser.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Exerciser Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exerciser.name ?: "No name",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "WEIGHT: ${exerciser.weight}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "AGE: ${exerciser.age}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            TextButton(onClick = {
                exerciser.id?.let {
                    navController.navigate("update_patient/$it")
                }
            }) {
                Text("Update", color = Color.Blue)
            }


            TextButton(onClick = { showDialog = true }) {
                Text("Delete", color = Color.Red)
            }
        }
    }
}
