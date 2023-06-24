package com.fkexample.cointicker.usecases

import com.fkexample.cointicker.mappers.fromEntityList
import com.fkexample.cointicker.presentation.models.Crypto
import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetAllCoinsUseCase(private val coinRepository: CoinRepository) {

    fun execute(): Flow<DataState<List<Crypto>>> = flow {
        coinRepository.getAllCoins()
            .catch { error ->
                emit(DataState.error(error))
            }
            .collect { cachedCoins ->
                val coins = fromEntityList(cachedCoins)

                emit(DataState.success(coins))
            }
    }
}