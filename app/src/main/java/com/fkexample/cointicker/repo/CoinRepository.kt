package com.fkexample.cointicker.repo

import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getAllCoins(): Flow<List<CryptoEntity>>
    suspend fun addOrRemoveFavCoin(favEntity: CryptoFavEntity)
    suspend fun getAllFavoriteCoins(): Flow<List<CryptoFavEntity>>
    suspend fun getFavById(assetId: String): CryptoFavEntity?
}