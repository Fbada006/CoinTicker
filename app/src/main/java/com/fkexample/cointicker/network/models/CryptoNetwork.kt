package com.fkexample.cointicker.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CryptoNetwork(
    @Json(name = "asset_id")
    val assetId: String,
    val name: String,
    @Json(name = "id_icon")
    val idIcon: String?,
)
