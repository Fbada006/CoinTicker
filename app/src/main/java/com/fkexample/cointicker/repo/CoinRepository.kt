package com.fkexample.cointicker.repo

import com.fkexample.cointicker.cache.models.CryptoEntity
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getAllCoins(): Flow<List<CryptoEntity>>
}