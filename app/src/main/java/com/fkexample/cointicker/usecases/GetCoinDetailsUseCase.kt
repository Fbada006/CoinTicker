package com.fkexample.cointicker.usecases

import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.ui.models.CryptoDetails
import com.fkexample.cointicker.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class GetCoinDetailsUseCase(private val coinRepository: CoinRepository) {

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