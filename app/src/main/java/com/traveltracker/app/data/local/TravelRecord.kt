package com.traveltracker.app.data.local

import kotlinx.serialization.Serializable

@Serializable
data class TravelRecord(
    val id: Long = 0,
    val country: String,
    val region: String,
    val entryDate: Long,
    val exitDate: Long,
    val notes: String? = null
)