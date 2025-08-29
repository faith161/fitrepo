//package com.example.fitapp.ui.theme.screens.exercisers
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.fitapp.data.ExerciserViewModel
//import com.example.fitapp.models.PersonalRecord
//import kotlinx.coroutines.launch
//
//@Composable
//fun UpdateRecordScreen(
//    navController: NavController,
//    viewModel: ExerciserViewModel = viewModel()
//) {
//    var record by remember { mutableStateOf<PersonalRecord?>(null) }
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(Unit) {
//        record = viewModel.getPersonalRecord()
//    }
//
//    record?.let {
//        var name by remember { mutableStateOf(it.name) }
//        var weight by remember { mutableStateOf(it.weight) }
//        var height by remember { mutableStateOf(it.height) }
//        var age by remember { mutableStateOf(it.age) }
//        var steps by remember { mutableStateOf(it.steps) }
//        var diagnosis by remember { mutableStateOf(it.diagnosis) }
//        var isMale by remember { mutableStateOf(it.isMale ?: true) }
//
//        Column(
//            Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Text("Update Your Record", style = MaterialTheme.typography.headlineSmall)
//
//            OutlinedTextField(
//                value = name,
//                onValueChange = { name = it },
//                label = { Text("Name") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            OutlinedTextField(
//                value = weight,
//                onValueChange = { weight = it },
//                label = { Text("Weight (kg)") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth()
//            )
//            OutlinedTextField(
//                value = height,
//                onValueChange = { height = it },
//                label = { Text("Height (cm)") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth()
//            )
//            OutlinedTextField(
//                value = age,
//                onValueChange = { age = it },
//                label = { Text("Age") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth()
//            )
//            OutlinedTextField(
//                value = steps,
//                onValueChange = { steps = it },
//                label = { Text("Steps") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth()
//            )
//            OutlinedTextField(
//                value = diagnosis,
//                onValueChange = { diagnosis = it },
//                label = { Text("Diagnosis") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Checkbox(
//                    checked = isMale,
//                    onCheckedChange = { isMale = it }
//                )
//                Text("Male")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Button(
//                onClick = {
//                    val updatedRecord = it.copy(
//                        name = name,
//                        weight = weight,
//                        height = height,
//                        age = age,
//                        steps = steps,
//                        diagnosis = diagnosis,
//                        isMale = isMale
//                    )
//                    coroutineScope.launch {
//                        viewModel.updatePersonalRecord(updatedRecord)
//                        navController.popBackStack()
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("💾 Save Changes")
//            }
//        }
//    } ?: run {
//        Box(
//            Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    }
//}
