package com.fkexample.cointicker.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity

@Database(entities = [CryptoEntity::class, CryptoFavEntity::class], version = 1)
abstract class TickerAppDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    companion object {
        const val COINS_DATABASE_NAME: String = "coins_db"
    }
}