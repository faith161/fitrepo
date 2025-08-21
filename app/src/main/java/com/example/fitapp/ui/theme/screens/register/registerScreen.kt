package com.example.fitapp.ui.theme.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitapp.R
import com.example.fitapp.data.AuthViewModel
import com.example.fitapp.navigation.ROUTE_LOGIN


@Composable

fun registerScreen(navController: NavController){
    var username by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
    val authViewModel:AuthViewModel = viewModel()

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF2196F3))
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter your Details",
                fontSize = 40.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.fit2),
                contentDescription = "Image Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Enter Username") },
                textStyle = TextStyle(color = Color.White),
                placeholder = { Text("Please enter username") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person icon") },
                modifier = Modifier.fillMaxWidth(0.8f),

                )

            OutlinedTextField(
                value = fullname, onValueChange = { fullname = it },
                label = { Text("Enter your Fullname") },
                placeholder = { Text("Please enter your Fullname") },
                textStyle = TextStyle(color = Color.White),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person icon") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )


            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Enter your Email") },
                textStyle = TextStyle(color = Color.White),
                placeholder = { Text("Please enter your Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email icon") },
                modifier = Modifier.fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Enter your Password") },
                placeholder = { Text("Please enter your Password") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock icon") },
                modifier = Modifier.fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            OutlinedTextField(
                value = confirmpassword, onValueChange = { confirmpassword = it },
                label = { Text("Confirm your password") },
                placeholder = { Text("Please confirm your password") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock icon") },
                modifier = Modifier.fillMaxWidth(0.8f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(10.dp))
            val context = LocalContext.current
            Button(
                onClick = {
                    authViewModel.signup(
                        username = username,
                        email = email,
                        fullname = fullname,
                        password = password,
                        confirmpassword = confirmpassword,
                        navController = navController,
                        context = context
                    )
                },
                colors = ButtonDefaults.buttonColors(Color.Blue),
                modifier = Modifier.fillMaxWidth(1.0f)
            )

            { Text("Register", color = Color.White) }
            Text(
                text = "If already registered, Login here",
                color = Color.Blue,
                modifier = Modifier.clickable { navController.navigate(ROUTE_LOGIN) })

        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun registerScreenPreview(){
    registerScreen(rememberNavController())
}