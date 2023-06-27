package com.fkexample.cointicker.usecases

import com.fkexample.cointicker.mappers.fromFavEntityList
import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.ui.models.Crypto
import com.fkexample.cointicker.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class GetAllFavoriteCoinsUseCase(private val coinRepository: CoinRepository) {

    operator fun invoke(): Flow<DataState<List<Crypto>>> = flow {
        coinRepository.getAllFavoriteCoins()
            .onStart {
                emit(DataState.loading())
            }.catch { error ->
                emit(DataState.error(error))
            }.collect { favEntityList ->
                val coins = fromFavEntityList(favEntityList)

                emit(DataState.success(coins))
            }
    }
}