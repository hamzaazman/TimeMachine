package com.hamzaazman.timemachine.di

import android.content.Context
import androidx.room.Room
import com.hamzaazman.timemachine.data.source.local.AppDatabase
import com.hamzaazman.timemachine.data.source.local.dao.HistoryEventDao
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
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "time_machine.db").build()

    @Provides
    fun provideDao(db: AppDatabase): HistoryEventDao = db.historyDao()
}