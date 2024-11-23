package com.fkexample.cointicker.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fkexample.cointicker.cache.models.CryptoAssetEntity
import com.fkexample.cointicker.cache.models.CryptoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CryptoEntity>): LongArray

    @Update
    suspend fun updateCoin(coin: CryptoEntity)

    @Query("""
        SELECT c.*,
        CASE WHEN f.asset_id IS NOT NULL THEN 1 ELSE 0 END AS isFavourite
        FROM coins c
        LEFT JOIN fav_asset_ids f ON c.asset_id = f.asset_id
    """)
    fun getAllCoins(): Flow<List<CryptoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCoin(favEntity: CryptoAssetEntity)

    @Delete
    suspend fun deleteCoinFromFav(favEntity: CryptoAssetEntity)

    @Query(
        """
    SELECT c.* FROM coins c 
    INNER JOIN fav_asset_ids f ON c.asset_id = f.asset_id
"""
    )
    fun getAllFavoriteCoins(): Flow<List<CryptoEntity>>

    @Query("SELECT * FROM fav_asset_ids WHERE asset_id LIKE :assetId")
    suspend fun getFavById(assetId: String): CryptoAssetEntity?

    @Query("SELECT * FROM coins WHERE asset_id LIKE :assetId")
    suspend fun getCoinById(assetId: String): CryptoEntity?
}