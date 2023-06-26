package com.fkexample.cointicker.repo.models

data class CryptoWithUrl(
    val assetId: String,
    val name: String,
    val cryptoUrl: String?,
    val isFavorite: Boolean
)