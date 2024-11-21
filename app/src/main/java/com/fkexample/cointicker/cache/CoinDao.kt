package com.fkexample.cointicker.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CryptoEntity>): LongArray

    @Query("SELECT * FROM coins")
    fun getAllCoins(): Flow<List<CryptoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCoin(favEntity: CryptoFavEntity)

    @Delete
    suspend fun deleteCoinFromFav(favEntity: CryptoFavEntity)

    @Query("SELECT * FROM fav_coins")
    fun getAllFavoriteCoins(): Flow<List<CryptoFavEntity>>

    @Query("SELECT * FROM fav_coins WHERE asset_id LIKE :assetId")
    suspend fun getFavById(assetId: String): CryptoFavEntity?

    @Query("SELECT * FROM coins WHERE asset_id LIKE :assetId")
    suspend fun getCoinById(assetId: String): CryptoEntity?
}