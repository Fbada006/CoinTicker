package com.fkexample.cointicker.presentation.models

data class Crypto(
    val assetId: String,
    val name: String,
    val cryptoUrl: String?,
    val timeAgo: String
)
