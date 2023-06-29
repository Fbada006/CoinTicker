package com.fkexample.cointicker.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CryptoDataNetwork(
    @Json(name = "asset_id") val assetId: String,
    @Json(name = "price_usd") val priceUsd: Double?,
)
