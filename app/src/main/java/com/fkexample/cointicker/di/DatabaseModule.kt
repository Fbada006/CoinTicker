package com.fkexample.cointicker.di

import android.content.Context
import androidx.room.Room
import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.db.TickerAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): TickerAppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TickerAppDatabase::class.java,
            TickerAppDatabase.COINS_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCoinDao(database: TickerAppDatabase): CoinDao = database.coinDao()
}