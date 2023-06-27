package com.fkexample.cointicker.usecases

import com.fkexample.cointicker.mappers.presentationModelToFavEntity
import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.ui.models.Crypto

class AddCoinToFavoriteUseCase(private val coinRepository: CoinRepository) {
    suspend operator fun invoke(crypto: Crypto) {
        coinRepository.addOrRemoveFavCoin(presentationModelToFavEntity(crypto))
    }
}