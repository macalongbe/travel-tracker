package com.traveltracker.app.data.repository

import com.traveltracker.app.data.local.TravelDataStore
import com.traveltracker.app.data.local.TravelRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelRepository @Inject constructor(
    private val travelDataStore: TravelDataStore
) {
    fun getAllRecords(): Flow<List<TravelRecord>> = travelDataStore.records

    suspend fun addRecord(record: TravelRecord): Long {
        travelDataStore.insertRecord(record)
        return record.id
    }

    suspend fun updateRecord(record: TravelRecord) {
        travelDataStore.updateRecord(record)
    }

    suspend fun deleteRecord(record: TravelRecord) {
        travelDataStore.deleteRecord(record)
    }

    suspend fun getRecordsByYear(year: Int): List<TravelRecord> {
        val allRecords = travelDataStore.records.first()
        
        val calendar = Calendar.getInstance()
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0)
        val startOfYear = calendar.timeInMillis
        
        calendar.set(year + 1, Calendar.JANUARY, 1, 0, 0, 0)
        val endOfYear = calendar.timeInMillis
        
        return allRecords.filter { record ->
            record.entryDate >= startOfYear && record.entryDate < endOfYear
        }
    }
}