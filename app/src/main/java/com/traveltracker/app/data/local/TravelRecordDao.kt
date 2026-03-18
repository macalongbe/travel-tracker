package com.traveltracker.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelRecordDao {
    @Query("SELECT * FROM travel_records ORDER BY entryDate DESC")
    fun getAllRecords(): Flow<List<TravelRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: TravelRecord): Long

    @Update
    suspend fun updateRecord(record: TravelRecord)

    @Delete
    suspend fun deleteRecord(record: TravelRecord)

    @Query("SELECT * FROM travel_records WHERE entryDate >= :startOfYear AND entryDate < :endOfYear ORDER BY entryDate DESC")
    suspend fun getRecordsByYear(startOfYear: Long, endOfYear: Long): List<TravelRecord>
}
