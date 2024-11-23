package com.fkexample.cointicker.usecases

//import com.fkexample.cointicker.mappers.presentationModelToFavEntity
import com.fkexample.cointicker.cache.models.CryptoAssetEntity
import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.ui.models.Crypto

/**
 * Use case for adding a coin to the favorites.
 * @property coinRepository The repository for managing coin data.
 */
class AddCoinToFavoriteUseCase(private val coinRepository: CoinRepository) {

    /**
     * Adds the specified [crypto] to the favorites.
     * @param crypto The [Crypto] object representing the coin to be added.
     */
    suspend operator fun invoke(crypto: Crypto) {
        coinRepository.addOrRemoveFavCoin(CryptoAssetEntity(crypto.assetId))
    }
}
