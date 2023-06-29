package com.fkexample.cointicker.network

import com.fkexample.cointicker.network.models.CryptoDataNetwork
import com.fkexample.cointicker.network.models.CryptoImageNetwork
import com.fkexample.cointicker.network.models.CryptoNetwork
import com.fkexample.cointicker.network.models.CryptoReferenceNetwork
import retrofit2.http.GET
import retrofit2.http.Path

interface TickerService {

    @GET("assets")
    suspend fun getAllCoins(): List<CryptoNetwork>

    @GET("assets/icons/128")
    suspend fun getAllIcons(): List<CryptoImageNetwork>

    @GET("assets/{asset_id}")
    suspend fun getCoinDetails(@Path("asset_id") assetId: String): List<CryptoDataNetwork>

    @GET("exchangerate/{asset_id_base}/{asset_id_quote}")
    suspend fun getCoinExchangeRate(@Path("asset_id_base") assetIdBase: String, @Path("asset_id_quote") assetIdQuote: String): CryptoReferenceNetwork
}