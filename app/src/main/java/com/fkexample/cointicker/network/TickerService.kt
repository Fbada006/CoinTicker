package com.fkexample.cointicker.network

import com.fkexample.cointicker.network.models.CryptoNetwork
import retrofit2.http.GET

interface TickerService {

    @GET("assets")
    suspend fun getAllCoins(): List<CryptoNetwork>
}