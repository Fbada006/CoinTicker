package com.fkexample.cointicker.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CryptoImageNetwork(
    @Json(name = "asset_id")
    val assetId: String,
    val url: String?
)
