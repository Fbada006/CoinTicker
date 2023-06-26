package com.fkexample.cointicker.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CryptoEntity>): LongArray

    @Query("SELECT * FROM coins")
    suspend fun getAllCoins(): List<CryptoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCoin(favEntity: CryptoFavEntity): LongArray

    @Delete
    suspend fun deleteCoinFromFav(favEntity: CryptoFavEntity)

    @Query("SELECT * FROM fav_coins")
    suspend fun getAllFavoriteCoins(): List<CryptoFavEntity>

    @Query("SELECT * FROM fav_coins WHERE asset_id LIKE :assetId")
    suspend fun getFavById(assetId: String): CryptoFavEntity?
}