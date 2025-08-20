package com.example.fitapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitapp.ui.theme.screens.exercisers.AddRecordsScreen
//import com.example.fitapp.ui.theme.screens.SplashScreen
import com.example.fitapp.ui.theme.screens.dashboard.DashboardScreen
import com.example.fitapp.ui.theme.screens.login.loginScreen
//import com.example.fitapp.ui.theme.screens.exerciser.AddRecordsScreen
// import com.example.fitapp.ui.theme.screens.exercisers.ExerciseListScreen
import com.example.fitapp.ui.theme.screens.exercisers.ExerciserListScreen
import com.example.fitapp.ui.theme.screens.patients.UpdateRecordsScreen
import com.example.fitapp.ui.theme.screens.register.registerScreen
import com.example.fitapp.ui.theme.screens.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController= rememberNavController(),startDestination:String= ROUTE_DASHBOARDSCREEN){
    NavHost(navController=navController, startDestination = startDestination){
        composable(ROUTE_SPLASH){ SplashScreen { navController.navigate(ROUTE_REGISTER){popUpTo(
            ROUTE_SPLASH){inclusive=true} } } }
        composable(ROUTE_REGISTER){ registerScreen(navController)}
        composable(ROUTE_LOGIN){ loginScreen(navController) }
        composable(ROUTE_DASHBOARDSCREEN){ DashboardScreen(navController) }
        composable(ROUTE_ADDRECORDSSCREEN){ AddRecordsScreen(navController) }
        composable(ROUTE_VIEWRECORDSSSCREEN){ ExerciserListScreen(navController) }
        composable(
            ROUTE_UPDATERECORDS,
            arguments = listOf(navArgument("exerciserId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciserId = backStackEntry.arguments?.getString("exerciserId")!!
            UpdateRecordsScreen(navController, exerciserId)
        }

    }
}


