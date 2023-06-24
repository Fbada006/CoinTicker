package com.fkexample.cointicker.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fkexample.cointicker.cache.models.CryptoEntity

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCoins(coins: List<CryptoEntity>): LongArray

    @Query("SELECT * FROM coins")
    suspend fun getAllCoins(): List<CryptoEntity>
}