package com.fkexample.cointicker.repo

import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity
import com.fkexample.cointicker.mappers.toEntityList
import com.fkexample.cointicker.network.TickerService
import com.fkexample.cointicker.repo.models.CryptoWithUrl
import com.fkexample.cointicker.ui.models.CryptoDetails
import com.fkexample.cointicker.utils.EURO_SYMBOL
import com.fkexample.cointicker.utils.GBP_SYMBOL
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
                Timber.e(e)
            }

            emit(coinDao.getAllCoins())

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
                Timber.e(e)
                emit(emptyList())
            }
        }.flowOn(dispatcher)
    }

    override suspend fun getFavById(assetId: String): CryptoFavEntity? {
        return coinDao.getFavById(assetId)
    }

    override suspend fun getCoinDetails(assetId: String): Flow<CryptoDetails?> {
        return flow {
            try {
                val data = tickerService.getCoinDetails(assetId).first()
                val euroAssetRate = tickerService.getCoinExchangeRate(assetIdBase = EURO_SYMBOL, assetIdQuote = assetId)
                val gbpAssetRate = tickerService.getCoinExchangeRate(assetIdBase = GBP_SYMBOL, assetIdQuote = assetId)
                val localCoinData = coinDao.getCoinById(assetId)

                val details =
                    if (localCoinData != null
                        && data.assetId == assetId
                        && euroAssetRate.quoteAssetId == assetId
                        && gbpAssetRate.quoteAssetId == assetId
                    ) {
                        CryptoDetails(
                            name = localCoinData.name,
                            dateCached = localCoinData.dateCached,
                            url = localCoinData.cryptoUrl,
                            priceUsd = data.priceUsd,
                            euroToAssetRate = euroAssetRate.rate,
                            gbpToAssetRate = gbpAssetRate.rate
                        )
                    } else {
                        null
                    }

                emit(details)
            } catch (e: Exception) {
                emit(null)
                Timber.e(e)
            }
        }.flowOn(dispatcher)
    }

    override suspend fun getCoinById(assetId: String): CryptoEntity? {
        return coinDao.getCoinById(assetId)
    }
}
