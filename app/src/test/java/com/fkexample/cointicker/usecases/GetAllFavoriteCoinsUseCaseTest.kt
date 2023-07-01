package com.fkexample.cointicker.usecases

import app.cash.turbine.test
import com.fkexample.cointicker.cache.models.CryptoFavEntity
import com.fkexample.cointicker.repo.CoinRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAllFavoriteCoinsUseCaseTest {

    private val coinRepository: CoinRepository = mockk()

    @Test
    fun `invoke should emit success state`() = runTest {
        val fakeCryptoFavEntityList: List<CryptoFavEntity> = listOf(
            CryptoFavEntity("BTC", "Bitcoin", "https://example.com/btc.png", System.currentTimeMillis()),
            CryptoFavEntity("ETH", "Ethereum", "https://example.com/eth.png", System.currentTimeMillis())
        )

        coEvery { coinRepository.getAllFavoriteCoins() } returns flowOf(fakeCryptoFavEntityList)

        val getAllFavoriteCoinsUseCase = GetAllFavoriteCoinsUseCase(coinRepository)

        getAllFavoriteCoinsUseCase().test {
            assertThat(awaitItem().loading).isTrue()
            assertThat(awaitItem().data?.size).isEqualTo(2)
            awaitComplete()
        }
    }

    @Test
    fun `invoke should emit error state on throwing exception`() = runTest {
        coEvery { coinRepository.getAllFavoriteCoins() } throws Exception("Error!")

        val getAllFavoriteCoinsUseCase = GetAllFavoriteCoinsUseCase(coinRepository)

        getAllFavoriteCoinsUseCase().test {
            awaitError()
            assertThat(awaitError().message).isEqualTo("Error!")
        }
    }
}