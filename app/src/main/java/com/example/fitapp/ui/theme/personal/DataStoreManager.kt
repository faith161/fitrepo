package com.example.fitapp.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.fitapp.models.PersonalRecord
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore("user_record")

class DataStoreManager(private val context: Context) {

    companion object {
        val NAME = stringPreferencesKey("name")
        val WEIGHT = stringPreferencesKey("weight")
        val HEIGHT = stringPreferencesKey("height")
        val AGE = stringPreferencesKey("age")
        val STEPS = stringPreferencesKey("steps")
        val DIAGNOSIS = stringPreferencesKey("diagnosis")
        val IMAGE = stringPreferencesKey("image")
        val IS_MALE = booleanPreferencesKey("isMale")
    }

    suspend fun saveRecord(record: PersonalRecord) {
        context.dataStore.edit { prefs ->
            prefs[NAME] = record.name
            prefs[WEIGHT] = record.weight.toString()
            prefs[HEIGHT] = record.height.toString()
            prefs[AGE] = record.age.toString()
            prefs[STEPS] = record.steps.toString()
            prefs[DIAGNOSIS] = record.diagnosis
            prefs[IMAGE] = record.imageUri ?: ""
            prefs[IS_MALE] = record.isMale ?: true
        }
    }

    suspend fun getRecord(): PersonalRecord {
        val prefs = context.dataStore.data.first()

        return PersonalRecord(
            name = prefs[NAME] ?: "",
            weight = prefs[WEIGHT] ?: "",
            height = prefs[HEIGHT] ?: "",
            age = prefs[AGE] ?: "",
            steps = prefs[STEPS] ?: "",
            diagnosis = prefs[DIAGNOSIS] ?: "",
            imageUri = prefs[IMAGE],
            isMale = prefs[IS_MALE] ?: true
        )
    }

    suspend fun clearRecord() {
        context.dataStore.edit { it.clear() }
    }
}
