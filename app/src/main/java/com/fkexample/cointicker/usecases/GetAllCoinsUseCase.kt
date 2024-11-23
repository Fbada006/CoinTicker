package com.fkexample.cointicker.usecases

import com.fkexample.cointicker.mappers.fromEntityList
import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.ui.models.Crypto
import com.fkexample.cointicker.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

/**
 * Use case for retrieving all coins.
 * @property coinRepository The repository for managing coin data.
 */
class GetAllCoinsUseCase(private val coinRepository: CoinRepository) {

    /**
     * Retrieves all coins and their favorite status.
     * @return A [Flow] emitting [DataState] containing a list of [Crypto] objects.
     */
    operator fun invoke(): Flow<DataState<List<Crypto>>> = flow {
        coinRepository.getAllCoins()
            .onStart {
            emit(DataState.loading())
        }.catch { error ->
            emit(DataState.error(error))
        }.collect { coins ->
            emit(DataState.success(fromEntityList(coins)))
        }
    }
}
