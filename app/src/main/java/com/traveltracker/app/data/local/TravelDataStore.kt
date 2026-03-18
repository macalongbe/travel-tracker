package com.traveltracker.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "travel_records")

@Singleton
class TravelDataStore @Inject constructor(
    private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private val RECORDS_KEY = stringPreferencesKey("travel_records")
    }

    val records: Flow<List<TravelRecord>> = context.dataStore.data.map { preferences ->
        val recordsJson = preferences[RECORDS_KEY] ?: "[]"
        try {
            json.decodeFromString<List<TravelRecord>>(recordsJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun insertRecord(record: TravelRecord) {
        context.dataStore.edit { preferences ->
            val currentRecords = try {
                json.decodeFromString<List<TravelRecord>>(preferences[RECORDS_KEY] ?: "[]").toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }
            
            val newId = if (record.id == 0L) {
                (currentRecords.maxOfOrNull { it.id } ?: 0) + 1
            } else {
                record.id
            }
            
            val recordWithId = record.copy(id = newId)
            
            val existingIndex = currentRecords.indexOfFirst { it.id == newId }
            if (existingIndex >= 0) {
                currentRecords[existingIndex] = recordWithId
            } else {
                currentRecords.add(recordWithId)
            }
            
            preferences[RECORDS_KEY] = json.encodeToString(currentRecords)
        }
    }

    suspend fun deleteRecord(record: TravelRecord) {
        context.dataStore.edit { preferences ->
            val currentRecords = try {
                json.decodeFromString<List<TravelRecord>>(preferences[RECORDS_KEY] ?: "[]").toMutableList()
            } catch (e: Exception) {
                return@edit
            }
            
            currentRecords.removeAll { it.id == record.id }
            preferences[RECORDS_KEY] = json.encodeToString(currentRecords)
        }
    }

    suspend fun updateRecord(record: TravelRecord) {
        insertRecord(record)
    }
}