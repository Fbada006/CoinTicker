package com.fkexample.cointicker.di

import com.fkexample.cointicker.network.TickerService
import com.fkexample.cointicker.utils.COIN_BASE_URL
import com.fkexample.cointicker.utils.HttpClient
import com.fkexample.cointicker.utils.MoshiBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesTickerService(retrofit: Retrofit): TickerService =
        retrofit.create(TickerService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(COIN_BASE_URL)
        .client(HttpClient.create())
        .addConverterFactory(MoshiConverterFactory.create(MoshiBuilder.create()))
        .build()
}