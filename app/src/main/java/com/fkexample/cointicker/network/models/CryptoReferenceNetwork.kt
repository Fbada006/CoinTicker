package com.fkexample.cointicker.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CryptoReferenceNetwork(
    @Json(name = "asset_id_base")
    val baseAssetId: String,
    @Json(name = "asset_id_quote")
    val quoteAssetId: String,
    val rate: Double
)
