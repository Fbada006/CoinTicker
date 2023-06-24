package com.fkexample.cointicker.repo

import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.mappers.toEntityList
import com.fkexample.cointicker.network.TickerService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CoinRepositoryImpl(
    private val tickerService: TickerService,
    private val coinDao: CoinDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoinRepository {

    override fun getAllCoins(): Flow<List<CryptoEntity>> {
        return flow {
            try {
                val coins = tickerService.getAllCoins()

                coinDao.insertCoins(toEntityList(coins))
            } catch (e: Exception) {
                // Something went wrong with the query to the API
                // Log the error and proceed
                Timber.d(e)
            }

            emit(coinDao.getAllCoins())

        }.flowOn(dispatcher)
    }
}
