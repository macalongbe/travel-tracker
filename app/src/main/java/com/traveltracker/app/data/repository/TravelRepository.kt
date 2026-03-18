package com.traveltracker.app.data.repository

import com.traveltracker.app.data.local.TravelRecord
import com.traveltracker.app.data.local.TravelRecordDao
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelRepository @Inject constructor(
    private val travelRecordDao: TravelRecordDao
) {
    fun getAllRecords(): Flow<List<TravelRecord>> = travelRecordDao.getAllRecords()

    suspend fun addRecord(record: TravelRecord): Long {
        return travelRecordDao.insertRecord(record)
    }

    suspend fun updateRecord(record: TravelRecord) {
        travelRecordDao.updateRecord(record)
    }

    suspend fun deleteRecord(record: TravelRecord) {
        travelRecordDao.deleteRecord(record)
    }

    suspend fun getRecordsByYear(year: Int): List<TravelRecord> {
        val calendar = Calendar.getInstance()
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0)
        val startOfYear = calendar.timeInMillis
        
        calendar.set(year + 1, Calendar.JANUARY, 1, 0, 0, 0)
        val endOfYear = calendar.timeInMillis
        
        return travelRecordDao.getRecordsByYear(startOfYear, endOfYear)
    }
}