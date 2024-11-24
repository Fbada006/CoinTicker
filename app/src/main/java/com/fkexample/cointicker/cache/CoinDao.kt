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
    suspend fun updateFavourite(coin: CryptoEntity)

    // This query does the following
    // 1.check if each coin exists in the favourites table using the LEFT JOIN
    // 2.sets the isFavourite flag of the entity to true (1) if the coin exists in favourites table otherwise false (0)
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

    // Query returns all the coins whose assets ids are in the favourites table
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