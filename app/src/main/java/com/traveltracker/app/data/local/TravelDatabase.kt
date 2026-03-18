package com.traveltracker.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TravelRecord::class],
    version = 1,
    exportSchema = false
)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun travelRecordDao(): TravelRecordDao
}
