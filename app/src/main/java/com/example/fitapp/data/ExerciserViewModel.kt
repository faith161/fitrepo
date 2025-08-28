package com.example.fitapp.data

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.fitapp.models.Exerciser
import com.example.fitapp.models.PersonalRecord
import com.example.fitapp.navigation.ROUTE_DASHBOARDSCREEN
import com.example.fitapp.navigation.ROUTE_VIEWRECORDSCREEN
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
class ExerciserViewModel(application: Application) : AndroidViewModel(application) {


        private val dataStore = DataStoreManager(application)

        private val _personalRecord = MutableStateFlow<PersonalRecord?>(null)
        val personalRecord: StateFlow<PersonalRecord?> = _personalRecord

        fun loadPersonalRecord() {
            viewModelScope.launch {
                val record = dataStore.getRecord()
                _personalRecord.value = if (record.name.isNotEmpty()) record else null
            }
        }

        fun savePersonalRecord(record: PersonalRecord) {
            viewModelScope.launch {
                dataStore.saveRecord(record)
                _personalRecord.value = record
            }
        }

        fun deletePersonalRecord() {
            viewModelScope.launch {
                dataStore.clearRecord()
                _personalRecord.value = null
            }
        }


    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dhfozknlw/image/upload"
    private val uploadPreset = "app_images"

    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes() ?: throw Exception("Image read failed")

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()

        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = OkHttpClient().newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Upload failed")

        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)

        return secureUrl ?: throw Exception("Failed to get image URL")
    }

    fun uploadRecords(
        imageUri: Uri?,
        name: String,
        weight: String,
        age: String,
        day: String,
        steps: String,
        diagnosis: String,
        context: Context,
        navController: NavController
    )



    {
        viewModelScope.launch(Dispatchers.IO) {
            if (name.isBlank() || weight.isBlank() || age.isBlank() || day.isBlank()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
                }
                return@launch
            }

            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }

                val ref = FirebaseDatabase.getInstance().getReference("Exercisers").push()
                val data = mapOf(
                    "id" to ref.key,
                    "name" to name,
                    "weight" to weight,
                    "age" to age,
                    "day" to day,
                    "steps" to steps,
                    "diagnosis" to diagnosis,
                    "imageUrl" to imageUrl
                )

                ref.setValue(data).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Record saved", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_VIEWRECORDSCREEN)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val _exercisers = mutableStateListOf<Exerciser>()
    val exerciser: List<Exerciser> = _exercisers

    fun fetchRecords(context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Exercisers")
        ref.get().addOnSuccessListener { snapshot ->
            _exercisers.clear()
            for (child in snapshot.children) {
                val exerciser = child.getValue(Exerciser::class.java)
                exerciser?.let { _exercisers.add(it) }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to load records", Toast.LENGTH_LONG).show()
        }
    }

    fun deleteRecord(id: String, context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Exercisers").child(id)
        ref.removeValue().addOnSuccessListener {
            _exercisers.removeAll { it.id == id }
        }.addOnFailureListener {
            Toast.makeText(context, "Delete failed", Toast.LENGTH_LONG).show()
        }
    }

    fun updateRecords(
        id: String,
        imageUri: Uri?,
        name: String,
        weight: String,
        age: String,
        day: String,
        steps: String,
        diagnosis: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ref = FirebaseDatabase.getInstance().getReference("Exercisers").child(id)
                val snapshot = ref.get().await()

                if (!snapshot.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Record not found", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                val existing = snapshot.getValue(Exerciser::class.java)
                val imageUrl = if (imageUri != null) {
                    uploadToCloudinary(context, imageUri)
                } else {
                    existing?.imageUrl ?: ""
                }

                val updatedData = mapOf(
                    "id" to id,
                    "name" to name,
                    "weight" to weight,
                    "age" to age,
                    "day" to day,
                    "steps" to steps,
                    "diagnosis" to diagnosis,
                    "imageUrl" to imageUrl
                )

                ref.setValue(updatedData).await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Record updated", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_DASHBOARDSCREEN)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun getPersonalRecord() {
        suspend fun getPersonalRecord(): PersonalRecord? {
            return dataStore.getRecord()
        }

    }


}
