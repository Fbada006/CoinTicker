package com.fkexample.cointicker.mappers

import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity
import com.fkexample.cointicker.repo.models.CryptoWithUrl
import com.fkexample.cointicker.ui.models.Crypto

fun toEntityList(data: List<CryptoWithUrl>): List<CryptoEntity> {
    return data.map { cryptoNetwork -> mapFromDomainModel(cryptoNetwork) }
}

fun fromEntityList(data: List<CryptoEntity>): List<Crypto> {
    return data.map { cryptoEntity -> mapToPresentationModel(cryptoEntity) }
}

fun fromFavEntityList(data: List<CryptoFavEntity>): List<Crypto> {
    return data.map { cryptoEntity -> favEntityToPresentationModel(cryptoEntity) }
}

private fun mapFromDomainModel(model: CryptoWithUrl) =
    CryptoEntity(
        assetId = model.assetId,
        name = model.name,
        cryptoUrl = model.cryptoUrl,
        dateCached = System.currentTimeMillis()
    )

private fun mapToPresentationModel(model: CryptoEntity) =
    Crypto(
        assetId = model.assetId,
        name = model.name,
        cryptoUrl = model.cryptoUrl,
        dateCached = model.dateCached
    )

fun favEntityToPresentationModel(model: CryptoFavEntity) =
    Crypto(
        assetId = model.assetId,
        name = model.name,
        cryptoUrl = model.cryptoUrl,
        dateCached = model.dateCached
    )

fun presentationModelToFavEntity(model: Crypto) =
    CryptoFavEntity(assetId = model.assetId, name = model.name, cryptoUrl = model.cryptoUrl, dateCached = model.dateCached)