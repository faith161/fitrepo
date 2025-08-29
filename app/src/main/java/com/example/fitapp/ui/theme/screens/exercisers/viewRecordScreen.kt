package com.example.fitapp.ui.theme.screens.exercisers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fitapp.data.ExerciserViewModel
import com.example.fitapp.models.PersonalRecord
import kotlinx.coroutines.launch
@Composable
fun ViewRecordsScreen(
    navController: NavController,
    viewModel: ExerciserViewModel = viewModel()
) {
    val context = LocalContext.current
    var record by remember { mutableStateOf<PersonalRecord?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3))
            .padding(16.dp)
    ) {
        LaunchedEffect(Unit) {
            isLoading = true
            try {
                val result = viewModel.getPersonalRecord()
                record = result
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }

        if (record == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading your record...", color = Color.White)
            }
            return
        }

        if (record!!.name.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No record found. Please add one.", color = Color.White)
            }
            return
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    "Your Health Summary",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))


                Spacer(modifier = Modifier.height(16.dp))

                Text("👤 Name: ${record!!.name}", color = Color.Black)
                Text("⚖️ Weight: ${record!!.weight} kg", color = Color.Black)
                Text("📏 Height: ${record!!.height} cm", color = Color.Black)
                Text("🎂 Age: ${record!!.age}", color = Color.Black)
                Text("👣 Steps: ${record!!.steps}", color = Color.Black)
                Text("🩺 Diagnosis: ${record!!.diagnosis}", color = Color.Black)
                Text("🚻 Gender: ${if (record!!.isMale == true) "Male" else "Female"}", color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                val weight = record!!.weight.toDoubleOrNull()
                val heightCm = record!!.height.toDoubleOrNull()
                val heightM = heightCm?.div(100)
                val bmi = if (weight != null && heightM != null && heightM > 0) {
                    weight / (heightM * heightM)
                } else null

                bmi?.let {
                    Text("📊 BMI: %.2f".format(it), color = Color.Black)
                }

                val age = record!!.age.toIntOrNull()
                val isMale = record!!.isMale ?: true

                val bmr = if (weight != null && heightCm != null && age != null) {
                    if (isMale)
                        10 * weight + 6.25 * heightCm - 5 * age + 5
                    else
                        10 * weight + 6.25 * heightCm - 5 * age - 161
                } else null

                bmr?.let {
                    val tdee = it * 1.55 // Moderate activity
                    Text("🔥 Estimated Calories Burned: %.0f kcal/day".format(tdee), color = Color.Black)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.deletePersonalRecord()
                            record = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🗑️ Delete My Record")
                }


            }
        }
    }
}
