package com.fkexample.cointicker.ui.models

data class Crypto(
    val assetId: String,
    val name: String,
    val cryptoUrl: String?,
    val timeAgo: String,
    val isFavorite: Boolean
)
