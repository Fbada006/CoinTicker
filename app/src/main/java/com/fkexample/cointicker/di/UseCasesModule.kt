package com.fkexample.cointicker.di

import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.usecases.AddCoinToFavoriteUseCase
import com.fkexample.cointicker.usecases.GetAllCoinsUseCase
import com.fkexample.cointicker.usecases.GetAllFavoriteCoinsUseCase
import com.fkexample.cointicker.usecases.GetCoinDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {

    @ViewModelScoped
    @Provides
    fun providesGetAllCoinsUseCase(
        repository: CoinRepository
    ): GetAllCoinsUseCase {
        return GetAllCoinsUseCase(repository)
    }

    @ViewModelScoped
    @Provides
    fun providesAddCoinToFavUseCase(
        repository: CoinRepository
    ): AddCoinToFavoriteUseCase {
        return AddCoinToFavoriteUseCase(repository)
    }

    @ViewModelScoped
    @Provides
    fun providesGetAllFavCoinsUseCase(
        repository: CoinRepository
    ): GetAllFavoriteCoinsUseCase {
        return GetAllFavoriteCoinsUseCase(repository)
    }

    @ViewModelScoped
    @Provides
    fun providesGetCoinDetailsUseCase(
        repository: CoinRepository
    ): GetCoinDetailsUseCase {
        return GetCoinDetailsUseCase(repository)
    }
}