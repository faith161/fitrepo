package com.example.fitapp.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.fitapp.models.Exerciser
import com.example.fitapp.navigation.ROUTE_DASHBOARDSCREEN
import com.example.fitapp.navigation.ROUTE_VIEWRECORDSSSCREEN
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.format
import java.io.InputStream

class ExerciserViewModel:ViewModel() {
    val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dhfozknlw/image/upload"
    val uploadPreset = "app_images"
    fun uploadExerciser(
        imageUri: Uri?,
        name: String,
        weight: String,
        age: String,
        day: String,
        context: Context,
        navController: NavController
    ) {

        viewModelScope.launch (Dispatchers.IO){
            if (name.isBlank() || weight.isBlank() || age.isBlank() || day.isBlank()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
                }
                return@launch
            }


            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val ref = FirebaseDatabase.getInstance().getReference("Exercisers").push()
                val exerciserData = mapOf(
                    "id" to ref.key,
                    "name" to name,
                    "weight" to weight,
                    "age" to age,
                    "day" to day,
                    "imageUrl" to imageUrl
                )
                ref.setValue(exerciserData).await()
               withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Records saved Successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_VIEWRECORDSSSCREEN)

                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"Records not saved",Toast.LENGTH_LONG).show()
                }
        }

        }
    }
    private fun uploadToCloudinary(context: Context,uri: Uri):String{
        val contentResolver=context.contentResolver
        val inputStream: InputStream? =contentResolver.openInputStream(uri)
        val fileBytes=inputStream?.readBytes()?:throw Exception("Image read failed")
        val requestBody=MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(),fileBytes))
            .addFormDataPart("upload_preset",uploadPreset).build()
        val request= Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response=OkHttpClient().newCall(request).execute()
        if (!response.isSuccessful)throw Exception("Upload failed")
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody?:"")?.groupValues?.get(1)
        return secureUrl?: throw Exception("Failed to get image url")

    }
    private val _exercisers = mutableStateListOf<Exerciser>()
    val exerciser:List<Exerciser> = _exercisers

    fun fetchRecords(context: Context){
        val ref = FirebaseDatabase.getInstance().getReference("Exercisers")
        ref.get().addOnSuccessListener{ snapshot ->
            _exercisers.clear()
            for(child in snapshot.children) {
                val exerciser = child.getValue(Exerciser::class.java)
                exerciser?.let { _exercisers.add(it) }
            }
        }.addOnFailureListener{
            Toast.makeText(context,"Failed to load records",Toast.LENGTH_LONG).show()
        }
    }
    fun deleteRecord(exerciserId: String,context: Context){
        val ref = FirebaseDatabase.getInstance().getReference("Exercisers").child(exerciserId)
        ref.removeValue().addOnSuccessListener{
            _exercisers.removeAll{it.id == exerciserId}
        }.addOnFailureListener{
            Toast.makeText(context,"Records not deleted",Toast.LENGTH_LONG).show()
        }

    }
    fun updateRecords(exerciserId:  String,
                      imageUri: Uri?,
                      name: String,
                      weight: String,
                      age: String,
                      day : String,
                      context: Context,
                      navController: NavController
    ){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val  imageUrl = imageUri?.let{uploadToCloudinary(context,it)}
                val updateRecords = mapOf(
                    "id" to  exerciserId,
                    "name" to name,
                    "weight" to weight,
                    "age" to age,
                    "day" to day,
                    "imageUrl" to imageUrl
                 )
                val ref = FirebaseDatabase.getInstance()
                    .getReference("Exercises").child(exerciserId)
                ref.setValue(updateRecords).await()
                fetchRecords(context)
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"Records updated successfully",Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_DASHBOARDSCREEN)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"Update failed",Toast.LENGTH_LONG).show()
                }
            }

        }

    }

    fun uploadRecords(
        value: Uri?,
        name: String,
        weight: String,
        age: String,
        day: String,
        context: Context,
        navController: NavController
    )

    {
        uploadExerciser(value, name, weight, age, day, context, navController)
    }


}