package com.traveltracker.app.di

import android.content.Context
import com.traveltracker.app.data.local.TravelDataStore
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
    fun provideTravelDataStore(
        @ApplicationContext context: Context
    ): TravelDataStore {
        return TravelDataStore(context)
    }
}