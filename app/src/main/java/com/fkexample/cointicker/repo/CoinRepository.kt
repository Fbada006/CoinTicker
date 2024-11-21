package com.fkexample.cointicker.repo

import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity
import com.fkexample.cointicker.ui.models.CryptoDetails
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getAllCoins(): Flow<List<CryptoEntity>>
    suspend fun addOrRemoveFavCoin(favEntity: CryptoFavEntity)
    suspend fun getAllFavoriteCoins(): Flow<List<CryptoFavEntity>>
    suspend fun getCoinById(assetId: String): CryptoEntity?
    suspend fun getCoinDetails(assetId: String): Flow<CryptoDetails?>
}