package com.fkexample.cointicker.network

import com.fkexample.cointicker.network.models.CryptoDataNetwork
import com.fkexample.cointicker.network.models.CryptoNetwork
import com.fkexample.cointicker.network.models.CryptoReferenceNetwork
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Represents a TickerService that provides various HTTP GET requests for coin-related data.
 */
interface TickerService {

    /**
     * Fetches a list of all available coins.
     * @return A list of [CryptoNetwork] objects representing the coins.
     */
    @GET("assets")
    suspend fun getAllCoins(): List<CryptoNetwork>

    /**
     * Fetches details of a specific coin.
     * @param assetId The asset ID of the coin.
     * @return A list of [CryptoDataNetwork] objects representing the coin details.
     */
    @GET("assets/{asset_id}")
    suspend fun getCoinDetails(@Path("asset_id") assetId: String): List<CryptoDataNetwork>

    /**
     * Fetches the exchange rate between two coins.
     * @param assetIdBase The asset ID of the base coin.
     * @param assetIdQuote The asset ID of the quote coin.
     * @return A [CryptoReferenceNetwork] object representing the exchange rate.
     */
    @GET("exchangerate/{asset_id_base}/{asset_id_quote}")
    suspend fun getCoinExchangeRate(
        @Path("asset_id_base") assetIdBase: String,
        @Path("asset_id_quote") assetIdQuote: String
    ): CryptoReferenceNetwork
}
