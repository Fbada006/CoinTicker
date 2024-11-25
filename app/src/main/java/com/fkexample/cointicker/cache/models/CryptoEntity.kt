package com.fkexample.cointicker.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CryptoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "asset_id")
    val assetId: String,
    val name: String,
    @ColumnInfo(name = "icon_id")
    val iconId: String?,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long,
    val isFavourite: Boolean = false,
)
