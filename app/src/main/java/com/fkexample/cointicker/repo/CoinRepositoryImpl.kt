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

/**
 * Implementation of the [CoinRepository] interface.
 * @property tickerService The service for fetching coin-related data.
 * @property coinDao The DAO for accessing and managing coin data in the local database.
 * @property dispatcher The CoroutineDispatcher to perform operations on.
 */
class CoinRepositoryImpl(
    private val tickerService: TickerService,
    private val coinDao: CoinDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoinRepository {

    /**
     * Retrieves a flow of all coins, combining data from the API and local database.
     * @return A flow emitting a list of [CryptoEntity] objects representing the coins.
     */
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

    /**
     * Adds or removes a favorite coin based on the provided [favEntity].
     * @param favEntity The [CryptoFavEntity] representing the favorite coin.
     */
    override suspend fun addOrRemoveFavCoin(favEntity: CryptoFavEntity) {
        val dbFav = getFavById(favEntity.assetId)
        if (dbFav != null) {
            // This is a favorite already, so remove it from the favorites
            coinDao.deleteCoinFromFav(favEntity)
        } else {
            coinDao.insertFavCoin(favEntity)
        }
    }

    /**
     * Retrieves a flow of all favorite coins from the local database.
     * @return A flow emitting a list of [CryptoFavEntity] objects representing the favorite coins.
     */
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

    /**
     * Retrieves the favorite coin with the specified [assetId] from the local database.
     * @param assetId The asset ID of the favorite coin.
     * @return The corresponding [CryptoFavEntity] if found, or null otherwise.
     */
    override suspend fun getFavById(assetId: String): CryptoFavEntity? {
        return coinDao.getFavById(assetId)
    }

    /**
     * Retrieves the details of a coin with the specified [assetId] from the API and local database.
     * @param assetId The asset ID of the coin.
     * @return A flow emitting a [CryptoDetails] object representing the coin details, or null if not found.
     */
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

    /**
     * Retrieves the coin with the specified [assetId] from the local database.
     * @param assetId The asset ID of the coin.
     * @return The corresponding [CryptoEntity] if found, or null otherwise.
     */
    override suspend fun getCoinById(assetId: String): CryptoEntity? {
        return coinDao.getCoinById(assetId)
    }
}
