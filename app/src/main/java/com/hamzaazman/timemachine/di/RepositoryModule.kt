package com.hamzaazman.timemachine.di

import com.hamzaazman.timemachine.data.repository.HistoryRepositoryImpl
import com.hamzaazman.timemachine.data.source.local.dao.HistoryEventDao
import com.hamzaazman.timemachine.data.source.remote.WikimediaApiService
import com.hamzaazman.timemachine.domain.repository.HistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideHistoryRepository(
        api: WikimediaApiService,
        dao: HistoryEventDao,
        @IoDispatcher io: CoroutineDispatcher
    ): HistoryRepository = HistoryRepositoryImpl(api, dao, io)
}