package com.example.fitapp.ui.theme.screens.patients

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fitapp.R
import com.example.fitapp.data.ExerciserViewModel
import com.example.fitapp.models.Exerciser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

@Composable
fun UpdateRecordsScreen(navController: NavController,exerciserId:String) {
    val exerciserViewModel: ExerciserViewModel = viewModel()
    var exerciser by remember { mutableStateOf<Exerciser?>(null) }
    LaunchedEffect(exerciserId) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("Exercisers").child(exerciserId)
        val snapshot = ref.get().await()
        exerciser = snapshot.getValue(Exerciser::class.java)?.apply {
            id = exerciserId
        }
    }

    if (exerciser==null){
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
            ){
            CircularProgressIndicator()
        }
        return
    }

    var name by remember { mutableStateOf(exerciser!!.name ?:"") }
    var weight by remember { mutableStateOf(exerciser!!.weight ?:"") }
    var age by remember { mutableStateOf(exerciser!!.age ?:"") }
    var day by remember { mutableStateOf(exerciser!!.day ?:"") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
          it?.let { uri -> imageUri.value = uri }
    }

    val context = LocalContext.current
    Box(){
        Image(painter = painterResource(id = R.drawable.pexel),
            contentDescription = "DashboardScreen pexels",
            contentScale = ContentScale.FillBounds)}

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Update Patient",
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            color = Color.Magenta, modifier = Modifier.fillMaxWidth()
        )
        Card(shape = CircleShape,
            modifier = Modifier.padding(10.dp).size(200.dp)) {
            AsyncImage(
                model = imageUri.value ?: exerciser!!.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp).clickable {
                    launcher.launch("image/*")
                })

        }
        Text(text = "Tap to Change Image")
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
            placeholder = { Text(text = "Please enter your age") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text(text = "Enter Patient Age") },
            placeholder = { Text(text = "Please enter age") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = day,
            onValueChange = { day = it },
            label = { Text(text = "Day") },
            placeholder = { Text(text = "Day") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            singleLine = false

        )
Spacer(modifier = Modifier.height(10.dp))
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Button(onClick = {navController.popBackStack()},
                colors = ButtonDefaults
                    .buttonColors(Color.Red))
            { Text(text = "GO BACK")
            }

            Button(onClick = {
                exerciserViewModel.updateRecords(
                    exerciserId,
                    imageUri.value,
                    name,
                    weight,
                    age,
                    day,
                    context,
                    navController
                )
            },
                colors = ButtonDefaults
                .buttonColors(Color.Blue)) { Text(text = "UPDATE")}
        }
    }

}

