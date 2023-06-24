package com.fkexample.cointicker.di

import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.network.TickerService
import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.repo.CoinRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @ViewModelScoped
    @Provides
    fun providesRepository(
        tickerService: TickerService,
        coinDao: CoinDao
    ): CoinRepository {
        return CoinRepositoryImpl(tickerService, coinDao)
    }
}