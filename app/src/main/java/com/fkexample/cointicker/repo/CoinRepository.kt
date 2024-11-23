package com.fkexample.cointicker.repo

import com.fkexample.cointicker.cache.models.CryptoAssetEntity
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.ui.models.CryptoDetails
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun updateCoin(coin: CryptoEntity)
    suspend fun getAllCoins(): Flow<List<CryptoEntity>>
    suspend fun addOrRemoveFavCoin(favEntity: CryptoAssetEntity)
    suspend fun getAllFavoriteCoins(): Flow<List<CryptoEntity>>
    suspend fun getCoinById(assetId: String): CryptoEntity?
//    suspend fun getFavById(assetId: String): CryptoFavEntity?
    suspend fun getCoinDetails(assetId: String): Flow<CryptoDetails?>
}