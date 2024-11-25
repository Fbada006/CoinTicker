package com.fkexample.cointicker.ui.models

import androidx.compose.runtime.Stable

@Stable
data class Crypto(
    val assetId: String,
    val name: String,
    val cryptoUrl: String?,
    val dateCached: Long,
    var isFavorite: Boolean = false
)
