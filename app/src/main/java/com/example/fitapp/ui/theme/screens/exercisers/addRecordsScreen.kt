package com.example.fitapp.ui.theme.screens.exercisers

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fitapp.R
import com.example.fitapp.data.ExerciserViewModel
import com.example.fitapp.models.PersonalRecord

@Composable
fun AddRecordsScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ExerciserViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }

    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri -> imageUri.value = uri }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add New Record",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1976D2)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .size(140.dp)
                    .clickable { imagePicker.launch("image/*") },
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                AsyncImage(
                    model = imageUri.value ?: R.drawable.ic_person,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Tap to upload image", color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            // Input Fields
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (cm)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = day, onValueChange = { day = it }, label = { Text("Day") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = steps, onValueChange = { steps = it }, label = { Text("Steps") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = diagnosis, onValueChange = { diagnosis = it }, label = { Text("Diagnosis") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Gender: ", modifier = Modifier.padding(end = 8.dp))
                Button(
                    onClick = { isMale = true },
                    colors = ButtonDefaults.buttonColors(if (isMale) Color.Blue else Color.LightGray)
                ) {
                    Text("Male")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { isMale = false },
                    colors = ButtonDefaults.buttonColors(if (!isMale) Color.Magenta else Color.LightGray)
                ) {
                    Text("Female")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Go Back")
                }

                Button(
                    onClick = {

                        viewModel.uploadRecords(
                            imageUri = imageUri.value,
                            name = name,
                            weight = weight,
                            age = age,
                            day = day,
                            steps = steps,
                            diagnosis = diagnosis,
                            context = context,
                            navController = navController
                        )

                        viewModel.savePersonalRecord(
                            PersonalRecord(
                                name = name,
                                weight = weight,
                                height = height,
                                age = age,
                                steps = steps,
                                diagnosis = diagnosis,
                                imageUri = imageUri.value?.toString(),
                                isMale = isMale
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                ) {
                    Text("Save Record")
                }
            }
        }
    }
}
