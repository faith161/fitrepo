package com.example.fitapp.ui.theme.screens

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitapp.R

@Composable
fun SplashScreen(onNavigateToNext: () -> Unit) {
    val splashScreenDuration = 3000L
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(splashScreenDuration)
        onNavigateToNext()
    }
    Box(modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center){
        Column (horizontalAlignment = Alignment.CenterHorizontally){
          Image(painter = painterResource(id = R.drawable.logo),
              contentDescription = "App logo",
              modifier = Modifier.size(300.dp))
            Text(text = "Welcome to Your Fit",
                color = Color.White,
                fontSize = 28.sp)
        }

    }

}