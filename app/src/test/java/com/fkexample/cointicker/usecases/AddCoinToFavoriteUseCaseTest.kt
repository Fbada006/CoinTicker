package com.fkexample.cointicker.usecases

import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoAssetEntity
import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.repo.CoinRepositoryImpl
import com.fkexample.cointicker.ui.models.Crypto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddCoinToFavoriteUseCaseTest {

    private val dao: CoinDao = mockk(relaxed = true)
    private val coinRepository: CoinRepository = CoinRepositoryImpl(mockk(), dao)

    @Test
    fun `adding coin to favorites succeeds when fav does not exist`() = runTest {
        val crypto = Crypto(assetId = "fugit", name = "Elmo Miranda", cryptoUrl = null, dateCached = 5665, isFavorite = false)
        coEvery { dao.getFavById(any()) } returns null

        val addCoinToFavoriteUseCase = AddCoinToFavoriteUseCase(coinRepository)

        addCoinToFavoriteUseCase(crypto)

        coVerify(exactly = 1) {
            dao.insertFavCoin(CryptoAssetEntity(crypto.assetId))
        }
    }

    @Test
    fun `adding coin to favorites succeeds when fav does exist`() = runTest {
        val favEntity = CryptoAssetEntity(assetId = "idque")
        coEvery { dao.getFavById(any()) } returns favEntity

        val addCoinToFavoriteUseCase = AddCoinToFavoriteUseCase(coinRepository)

        addCoinToFavoriteUseCase(Crypto(assetId = "idque", name = "Dolores McGuire", cryptoUrl = null, dateCached = 6697))

        coVerify(exactly = 1) {
            dao.deleteCoinFromFav(favEntity)
        }
    }
}