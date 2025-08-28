package com.example.fitapp.ui.theme.screens.exercisers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    LaunchedEffect(Unit) {
        val result = viewModel.getPersonalRecord()
//        record = result
    }


    if (record == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading your record...")
        }
        return
    }

    if (record!!.name.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No record found. Please add one.")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Health Summary", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        record!!.imageUri?.let {
            AsyncImage(
                model = it,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("👤 Name: ${record!!.name}")
        Text("⚖️ Weight: ${record!!.weight} kg")
        Text("📏 Height: ${record!!.height} cm")
        Text("🎂 Age: ${record!!.age}")
        Text("👣 Steps: ${record!!.steps}")
        Text("🩺 Diagnosis: ${record!!.diagnosis}")
        Text("🚻 Gender: ${if (record!!.isMale == true) "Male" else "Female"}")

        Spacer(modifier = Modifier.height(16.dp))

        val weight = record!!.weight.toDoubleOrNull()
        val heightCm = record!!.height.toDoubleOrNull()
        val heightM = heightCm?.div(100)
        val bmi = if (weight != null && heightM != null && heightM > 0) {
            weight / (heightM * heightM)
        } else null

        bmi?.let {
            Text("📊 BMI: %.2f".format(it))
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
            Text("🔥 Estimated Calories Burned: %.0f kcal/day".format(tdee))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val viewModelScope = null
                viewModelScope?.launch {
                    viewModel.deletePersonalRecord()
                    record = null
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("🗑️ Delete My Record")
        }
    }
}
