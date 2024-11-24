package com.fkexample.cointicker.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_asset_ids")
data class CryptoAssetEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "asset_id")
    val assetId: String,
)
