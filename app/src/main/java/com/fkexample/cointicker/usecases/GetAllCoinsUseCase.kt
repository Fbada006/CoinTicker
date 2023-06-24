package com.fkexample.cointicker.usecases

import com.fkexample.cointicker.mappers.fromEntityList
import com.fkexample.cointicker.presentation.models.Crypto
import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class GetAllCoinsUseCase(private val coinRepository: CoinRepository) {

    operator fun invoke(): Flow<DataState<List<Crypto>>> = flow {
        coinRepository.getAllCoins()
            .onStart {
                emit(DataState.loading())
            }
            .catch { error ->
                emit(DataState.error(error))
            }
            .collect { cachedCoins ->
                val coins = fromEntityList(cachedCoins)

                emit(DataState.success(coins))
            }
    }
}