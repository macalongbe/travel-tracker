package com.traveltracker.app.di

import android.content.Context
import androidx.room.Room
import com.traveltracker.app.data.local.TravelDatabase
import com.traveltracker.app.data.local.TravelRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTravelDatabase(
        @ApplicationContext context: Context
    ): TravelDatabase {
        return Room.databaseBuilder(
            context,
            TravelDatabase::class.java,
            "travel_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTravelRecordDao(database: TravelDatabase): TravelRecordDao {
        return database.travelRecordDao()
    }
}