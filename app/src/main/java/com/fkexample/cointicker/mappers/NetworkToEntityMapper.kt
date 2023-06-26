package com.fkexample.cointicker.mappers

import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.repo.models.CryptoWithUrl
import com.fkexample.cointicker.ui.models.Crypto
import com.fkexample.cointicker.utils.DateUtils

fun toEntityList(data: List<CryptoWithUrl>): List<CryptoEntity> {
    return data.map { cryptoNetwork -> mapFromDomainModel(cryptoNetwork) }
}

fun fromEntityList(data: List<CryptoEntity>): List<Crypto> {
    return data.map { cryptoEntity -> mapToPresentationModel(cryptoEntity) }
}

private fun mapFromDomainModel(model: CryptoWithUrl) =
    CryptoEntity(
        assetId = model.assetId,
        name = model.name,
        cryptoUrl = model.cryptoUrl,
        dateCached = System.currentTimeMillis(),
        isFavorite = model.isFavorite
    )

private fun mapToPresentationModel(model: CryptoEntity) =
    Crypto(
        assetId = model.assetId,
        name = model.name,
        cryptoUrl = model.cryptoUrl,
        timeAgo = DateUtils.getTimeAgo(model.dateCached),
        isFavorite = model.isFavorite
    )