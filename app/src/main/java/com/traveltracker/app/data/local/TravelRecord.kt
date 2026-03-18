package com.traveltracker.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travel_records")
data class TravelRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val country: String,
    val region: String,
    val entryDate: Long,
    val exitDate: Long,
    val notes: String? = null
)
