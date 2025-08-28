package com.example.fitapp.data
import androidx.lifecycle.ViewModel
import com.example.fitapp.models.PersonalRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PersonalRecordViewModel : ViewModel() {

    private val _records = MutableStateFlow<List<PersonalRecord>>(emptyList())
    val records: StateFlow<List<PersonalRecord>> = _records

    fun savePersonalRecord(record: PersonalRecord) {
        val currentList = _records.value.toMutableList()
        currentList.add(record)
        _records.value = currentList
    }
}
