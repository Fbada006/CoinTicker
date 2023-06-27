package com.fkexample.cointicker.repo

import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity
import com.fkexample.cointicker.mappers.toEntityList
import com.fkexample.cointicker.network.TickerService
import com.fkexample.cointicker.repo.models.CryptoWithUrl
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
                val icons = tickerService.getAllIcons()
                val iconMap = icons.associateBy { icon -> icon.assetId }
                val coins = tickerService.getAllCoins()
                val coinsWithUrl = coins.map { coin ->
                    CryptoWithUrl(assetId = coin.assetId, name = coin.name, cryptoUrl = iconMap[coin.assetId]?.url)
                }

                coinDao.insertCoins(toEntityList(coinsWithUrl))
            } catch (e: Exception) {
                // Something went wrong with the query to the API
                // Log the error and proceed
                Timber.d(e)
            }

            emit(coinDao.getAllCoins())

        }.flowOn(dispatcher)
    }

    override fun getAllCoinsFromDb(): Flow<List<CryptoEntity>> {
        return flow {
            try {
                emit(coinDao.getAllCoins())
            } catch (e: Exception) {
                emit(emptyList())
            }
        }.flowOn(dispatcher)
    }

    override suspend fun addOrRemoveFavCoin(favEntity: CryptoFavEntity) {
        val dbFav = getFavById(favEntity.assetId)
        if (dbFav != null) {
            // This is a favorite already so remove it from the favorites
            coinDao.deleteCoinFromFav(favEntity)
        } else {
            coinDao.insertFavCoin(favEntity)
        }
    }

    override suspend fun getAllFavoriteCoins(): Flow<List<CryptoFavEntity>> {
        return flow {
            try {
                emit(coinDao.getAllFavoriteCoins())
            } catch (e: Exception) {
                Timber.d(e)
                emit(emptyList())
            }
        }.flowOn(dispatcher)
    }

    override suspend fun getFavById(assetId: String): CryptoFavEntity? {
        return coinDao.getFavById(assetId)
    }
}
