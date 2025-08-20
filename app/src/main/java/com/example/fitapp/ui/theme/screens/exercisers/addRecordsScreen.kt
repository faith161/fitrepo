package com.example.fitapp.ui.theme.screens.exercisers

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.fitapp.R
import com.example.fitapp.data.ExerciserViewModel

@Composable
fun AddRecordsScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    val imageUri = rememberSaveable() { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri.value = it }
        }
    val exerciserViewModel: ExerciserViewModel = viewModel()
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFADD86)
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ADD NEW RECORDS",
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                color = Color.Magenta, modifier = Modifier.fillMaxWidth()
            )
            Card(
                shape = CircleShape,
                modifier = Modifier.padding(10.dp).size(200.dp)
            ) {
                AsyncImage(
                    model = imageUri.value ?: R.drawable.ic_person,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(200.dp).clickable {
                        launcher.launch("image/*")
                    })

            }
            Text(text = "Upload Picture Here")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Enter Patient Name") },
                placeholder = { Text(text = "Please enter patient name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text(text = "Enter Your Weight") },
                placeholder = { Text(text = "Please enter your weight") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text(text = "Enter Patient Age") },
                placeholder = { Text(text = "Please enter age") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = day,
                onValueChange = { day = it },
                label = { Text(text = "Enter the day of the week") },
                placeholder = { Text(text = "Please enter the day") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults
                        .buttonColors(Color.Red)
                )
                {
                    Text(text = "GO BACK")
                }

                Button(
                    onClick = {
                        exerciserViewModel.uploadRecords(
                            imageUri.value,
                            name,
                            weight,
                            age,
                            day,
                            context,
                            navController
                        )

                    }, colors = ButtonDefaults
                        .buttonColors(Color.Blue)
                ) { Text(text = "SAVE RECORDS") }
            }
        }

    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddRecordsScreenPreview(){
    AddRecordsScreen(rememberNavController())
}