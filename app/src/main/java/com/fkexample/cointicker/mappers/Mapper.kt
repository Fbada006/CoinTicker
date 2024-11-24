/**
 * This file contains mapper functions to convert between different models used in the coin ticker application.
 * The mapping functions handle conversion between cache models, repository models, and UI models.
 */

package com.fkexample.cointicker.mappers

import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.network.models.CryptoNetwork
import com.fkexample.cointicker.ui.models.Crypto

/**
 * Converts a list of [CryptoNetwork] objects to a list of [CryptoEntity] objects.
 *
 * @param data The list of [CryptoNetwork] objects to be converted.
 * @return The list of [CryptoEntity] objects.
 */
fun toEntityList(data: List<CryptoNetwork>): List<CryptoEntity> {
    return data.map { cryptoNetwork -> mapFromDomainModel(cryptoNetwork) }
}

/**
 * Converts a list of [CryptoEntity] objects to a list of [Crypto] objects for UI presentation.
 *
 * @param data The list of [CryptoEntity] objects to be converted.
 * @return The list of [Crypto] objects.
 */
fun fromEntityList(data: List<CryptoEntity>): List<Crypto> {
    return data.map { cryptoEntity -> mapToPresentationModel(cryptoEntity) }
}

/**
 * Converts a [CryptoNetwork] object to a [CryptoEntity] object for cache storage.
 *
 * @param model The [CryptoNetwork] object to be converted.
 * @return The corresponding [CryptoEntity] object.
 */
private fun mapFromDomainModel(model: CryptoNetwork) =
    CryptoEntity(
        assetId = model.assetId,
        name = model.name,
        cryptoUrl = "${model.iconId?.replace("-","")}.png",
        dateCached = System.currentTimeMillis()
    )

/**
 * Converts a [CryptoEntity] object to a [Crypto] object for UI presentation.
 *
 * @param model The [CryptoEntity] object to be converted.
 * @return The corresponding [Crypto] object.
 */
private fun mapToPresentationModel(model: CryptoEntity) =
    Crypto(
        assetId = model.assetId,
        name = model.name,
        cryptoUrl = model.cryptoUrl,
        dateCached = model.dateCached,
        isFavorite = model.isFavourite
    )