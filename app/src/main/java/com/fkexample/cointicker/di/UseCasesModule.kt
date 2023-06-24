package com.fkexample.cointicker.di

import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.usecases.GetAllCoinsUseCase
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
}