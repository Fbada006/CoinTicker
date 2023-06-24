package com.fkexample.cointicker.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CryptoEntity(
    @PrimaryKey(autoGenerate = false)
    val assetId: String,
    val name: String,
    val idIcon: String?,
)
