package com.example.fitapp.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
//import com.example.fitapp.models.UserModel
import com.example.fitapp.navigation.ROUTE_DASHBOARDSCREEN
import com.example.fitapp.navigation.ROUTE_LOGIN
import com.example.fitapp.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel:ViewModel() {
    private val auth:FirebaseAuth= FirebaseAuth.getInstance()

    fun signup(username:String,
               fullname:String,
               email:String,
               password:String,
               confirmpassword:String,
               navController: NavController,
               context: Context) {
        if (username.isBlank() || fullname.isBlank() ||  email.isBlank() || password.isBlank() || confirmpassword.isBlank()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_LONG).show()
            return
        }
        if (password != confirmpassword) {
            Toast.makeText(context, "Password do not match", Toast.LENGTH_LONG).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: ""
                val user = UserModel(username = username, fullname=fullname ,email = email, userId = userId)

                saveUserToDatabase(user, navController, context)
            } else {
                Toast.makeText(
                    context, task.exception?.message ?: "Registration failed", Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun saveUserToDatabase(user: UserModel, navController: NavController, context: Context){
        val dbRef = FirebaseDatabase.getInstance().getReference("User/${user.userId}")
        dbRef.setValue(user).addOnCompleteListener {
            task ->
            if (task.isSuccessful){
                Toast.makeText(context,"User Registered succcessfully",
                    Toast.LENGTH_LONG).show()
                navController.navigate(ROUTE_LOGIN){
                    popUpTo(0)
                }

            }else{

                  Toast.makeText(context, task.exception?.message ?: "Failed to save user",Toast.LENGTH_LONG).show()
            }
        }
    }


    fun login(email:String,password: String,navController: NavController,context: Context){
        if (email.isBlank() || password.isBlank()){
            Toast.makeText(context,"Email and Password required",Toast.LENGTH_LONG).show()
            return
        }
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                task->
            if (task.isSuccessful){
                Toast.makeText(context,"Login successfully",Toast.LENGTH_LONG).show()
                navController.navigate(ROUTE_DASHBOARDSCREEN) {
                    popUpTo(0)
                }
            }else{
                Toast.makeText(context,task.exception?.message ?:"Login failed",
                    Toast.LENGTH_LONG).show()
            }
        }


    }
    fun logout(context: Context, navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(context, "Logged Out Successfully", Toast.LENGTH_LONG).show()
        navController.navigate(ROUTE_LOGIN) {
            popUpTo(0) { inclusive = true }
        }
    }

    }
