/**
 * This file contains mapper functions to convert between different models used in the coin ticker application.
 * The mapping functions handle conversion between cache models, repository models, and UI models.
 */

package com.fkexample.cointicker.mappers

import com.fkexample.cointicker.cache.models.CryptoEntity
//import com.fkexample.cointicker.cache.models.CryptoFavEntity
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

///**
// * Converts a list of [CryptoFavEntity] objects to a list of [Crypto] objects for UI presentation.
// *
// * @param data The list of [CryptoFavEntity] objects to be converted.
// * @return The list of [Crypto] objects.
// */
//fun fromFavEntityList(data: List<CryptoFavEntity>): List<Crypto> {
//    return data.map { cryptoEntity -> favEntityToPresentationModel(cryptoEntity) }
//}

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

///**
// * Converts a [CryptoFavEntity] object to a [Crypto] object for UI presentation.
// *
// * @param model The [CryptoFavEntity] object to be converted.
// * @return The corresponding [Crypto] object.
// */
//fun favEntityToPresentationModel(model: CryptoFavEntity) =
//    Crypto(
//        assetId = model.assetId,
//        name = model.name,
//        cryptoUrl = model.cryptoUrl,
//        dateCached = model.dateCached
//    )

///**
// * Converts a [Crypto] object to a [CryptoFavEntity] object for storage as a favorite coin.
// *
// * @param model The [Crypto] object to be converted.
// * @return The corresponding [CryptoFavEntity] object.
// */
//fun presentationModelToFavEntity(model: Crypto) =
//    CryptoFavEntity(assetId = model.assetId, name = model.name, cryptoUrl = model.cryptoUrl, dateCached = model.dateCached)

/**
 * Converts a [Crypto] object to a [CryptoEntity] object for storage.
 *
 * @param model The [Crypto] object to be converted.
 * @return The corresponding [CryptoEntity] object.
 */
fun presentationModelToEntity(model: Crypto) =
    CryptoEntity(assetId = model.assetId, name = model.name, cryptoUrl = model.cryptoUrl, dateCached = model.dateCached, isFavourite = model.isFavorite)