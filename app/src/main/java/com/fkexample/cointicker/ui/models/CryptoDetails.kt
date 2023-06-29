package com.fkexample.cointicker.ui.models

data class CryptoDetails(
    val name: String,
    val dateCached: Long,
    val url: String?,
    val priceUsd: Double?,
    val euroToAssetRate: Double?,
    val gbpToAssetRate: Double?
)
