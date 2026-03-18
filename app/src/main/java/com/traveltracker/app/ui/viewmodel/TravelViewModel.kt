package com.traveltracker.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traveltracker.app.data.local.TravelRecord
import com.traveltracker.app.data.repository.TravelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class TravelUiState(
    val records: List<TravelRecord> = emptyList(),
    val stats: Map<String, Int> = emptyMap(),
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TravelViewModel @Inject constructor(
    private val repository: TravelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TravelUiState())
    val uiState: StateFlow<TravelUiState> = _uiState.asStateFlow()

    init {
        loadRecords()
        calculateStats(_uiState.value.selectedYear)
    }

    private fun loadRecords() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getAllRecords().collect { records ->
                _uiState.value = _uiState.value.copy(
                    records = records,
                    isLoading = false
                )
            }
        }
    }

    fun addRecord(record: TravelRecord) {
        viewModelScope.launch {
            try {
                repository.addRecord(record)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateRecord(record: TravelRecord) {
        viewModelScope.launch {
            try {
                repository.updateRecord(record)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteRecord(record: TravelRecord) {
        viewModelScope.launch {
            try {
                repository.deleteRecord(record)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun setSelectedYear(year: Int) {
        _uiState.value = _uiState.value.copy(selectedYear = year)
        calculateStats(year)
    }

    fun calculateStats(year: Int) {
        viewModelScope.launch {
            val records = repository.getRecordsByYear(year)
            val stats = mutableMapOf<String, Int>()
            
            records.forEach { record ->
                val days = ((record.exitDate - record.entryDate) / (1000 * 60 * 60 * 24)).toInt() - 1
                if (days > 0) {
                    val key = "${record.country} - ${record.region}"
                    stats[key] = (stats[key] ?: 0) + days
                }
            }
            
            _uiState.value = _uiState.value.copy(stats = stats)
        }
    }
}
