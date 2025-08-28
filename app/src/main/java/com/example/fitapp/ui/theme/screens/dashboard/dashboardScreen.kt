package com.example.fitapp.ui.theme.screens.dashboard

import android.content.Intent
import android.graphics.Paint.Align
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitapp.R
import com.example.fitapp.data.AuthViewModel
import com.example.fitapp.navigation.ROUTE_ADDRECORDSSCREEN
import com.example.fitapp.navigation.ROUTE_VIEWRECORDSCREEN
import kotlinx.coroutines.selects.select

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val selectedItem = remember { mutableStateOf(1) }
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val showDialog = remember { mutableStateOf(false) }
    Scaffold(bottomBar = {
        NavigationBar(containerColor = Color.Magenta) {
            NavigationBarItem(
                selected = selectedItem.value == 0,
                onClick = { selectedItem.value = 0 },
                icon = { Icon(Icons.Filled.Share, contentDescription = "Share") },
                label = { Text(text = "Share") },
                alwaysShowLabel = true
            )
            NavigationBarItem(
                selected = selectedItem.value == 1,
                onClick = {
                    selectedItem.value = 1
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:0700000000")
                    }
                    context.startActivity(intent)
                },
                icon = { Icon(Icons.Filled.Phone, contentDescription = "Phone") },
                label = { Text(text = "Phone") },
                alwaysShowLabel = true
            )
            NavigationBarItem(
                selected = selectedItem.value == 2,
                onClick = {
                    selectedItem.value = 2
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:info@emobilis.edu")
                        putExtra(Intent.EXTRA_SUBJECT, "Inquiry")
                        putExtra(Intent.EXTRA_TEXT, "How do I open a bank account")
                    }
                    context.startActivity(intent)
                },
                icon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
                label = { Text(text = "Email") },
                alwaysShowLabel = true
            )
        }
    })
    { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Image(
                painter = painterResource(id = R.drawable.trees),
                contentDescription = "DashboardScreen trees",
                contentScale = ContentScale.FillBounds
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = "Daily Records") },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Person, contentDescription = "Person")
                }
                IconButton(onClick = {
                    showDialog.value = true
                }) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Logout")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Magenta,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            verticalArrangement = Arrangement.Center, modifier = Modifier.height(550.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text(text = "Logout") },
                    text = { Text(text = "Are you sure you want to log out?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog.value = false
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog.value = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
            Text(
                text = "Welcome to your Daily Records",
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                color = Color.Blue,
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier.wrapContentWidth()) {
                Card(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(20.dp)
                        .clickable { navController.navigate(ROUTE_ADDRECORDSSCREEN) },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(Color.White),
                ) {
                    Row(
                        modifier = Modifier
                            .height(140.dp)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Add Records",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                }


                Spacer(modifier = Modifier.width(20.dp))
                Card(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(20.dp)
                        .clickable { navController.navigate(ROUTE_VIEWRECORDSCREEN) },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(Color.White),
                ) {
                    Row(
                        modifier = Modifier
                            .height(140.dp)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "View Records",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

//        }
//    }


@Preview(showBackground = true, showSystemUi = true)
@Composable
    fun DashboardScreenPreview() {
        DashboardScreen(rememberNavController())
    }
