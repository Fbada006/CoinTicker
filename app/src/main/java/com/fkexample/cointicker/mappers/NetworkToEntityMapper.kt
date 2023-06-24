package com.fkexample.cointicker.mappers

import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.network.models.CryptoNetwork
import com.fkexample.cointicker.presentation.models.Crypto
import com.fkexample.cointicker.utils.DateUtils

fun toEntityList(data: List<CryptoNetwork>): List<CryptoEntity> {
    return data.map { cryptoNetwork -> mapFromDomainModel(cryptoNetwork) }
}

fun fromEntityList(data: List<CryptoEntity>): List<Crypto> {
    return data.map { cryptoEntity -> mapToPresentationModel(cryptoEntity) }
}

private fun mapFromDomainModel(model: CryptoNetwork) =
    CryptoEntity(assetId = model.assetId, name = model.name, idIcon = model.idIcon, dateCached = System.currentTimeMillis())

private fun mapToPresentationModel(model: CryptoEntity) =
    Crypto(assetId = model.assetId, name = model.name, idIcon = model.idIcon, timeAgo = DateUtils.getTimeAgo(model.dateCached))