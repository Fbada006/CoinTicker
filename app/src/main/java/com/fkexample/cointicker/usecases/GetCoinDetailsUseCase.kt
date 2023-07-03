package com.fkexample.cointicker.usecases

import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.ui.models.CryptoDetails
import com.fkexample.cointicker.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

/**
 * Use case for retrieving coin details by asset ID.
 * @property coinRepository The repository for managing coin data.
 */
class GetCoinDetailsUseCase(private val coinRepository: CoinRepository) {

    /**
     * Retrieves coin details for the specified asset ID.
     * @param assetId The asset ID of the coin.
     * @return A [Flow] emitting [DataState] containing [CryptoDetails] object or an error.
     */
    operator fun invoke(assetId: String): Flow<DataState<CryptoDetails?>> = flow {
        coinRepository.getCoinDetails(assetId)
            .onStart {
                emit(DataState.loading())
            }.catch { error ->
                emit(DataState.error(error))
            }.collect { details ->
                emit(DataState.success(details))
            }
    }
}