package com.fkexample.cointicker.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoAssetEntity
import com.fkexample.cointicker.cache.models.CryptoEntity

@Database(entities = [CryptoEntity::class, CryptoAssetEntity::class], version = 1)
abstract class TickerAppDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    companion object {
        const val COINS_DATABASE_NAME: String = "coins_db"
    }
}