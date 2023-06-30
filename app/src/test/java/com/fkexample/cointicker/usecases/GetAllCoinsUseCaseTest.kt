package com.fkexample.cointicker.usecases

import app.cash.turbine.test
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity
import com.fkexample.cointicker.repo.CoinRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAllCoinsUseCaseTest {

    private val coinRepository: CoinRepository = mockk()

    @Test
    fun `invoke should emit success state with combined data`() = runTest {
        val fakeCryptoEntityList: List<CryptoEntity> = listOf(
            CryptoEntity("BTC", "Bitcoin", "https://example.com/btc.png", System.currentTimeMillis()),
            CryptoEntity("ETH", "Ethereum", "https://example.com/eth.png", System.currentTimeMillis())
        )

        val fakeCryptoFavEntityList: List<CryptoFavEntity> = listOf(
            CryptoFavEntity("BTC", "Bitcoin", "https://example.com/btc.png", System.currentTimeMillis()),
            CryptoFavEntity("ETH", "Ethereum", "https://example.com/eth.png", System.currentTimeMillis())
        )

        coEvery { coinRepository.getAllCoins() } returns flowOf(fakeCryptoEntityList)
        coEvery { coinRepository.getAllFavoriteCoins() } returns flowOf(fakeCryptoFavEntityList)

        val getAllCoinsUseCase = GetAllCoinsUseCase(coinRepository)

        getAllCoinsUseCase().test {
            assertThat(awaitItem().loading).isTrue()
            assertThat(awaitItem().data?.size).isEqualTo(2)
            awaitComplete()
        }
    }

    @Test
    fun `invoke should emit error state on throwing exception`() = runTest {

        val fakeCryptoFavEntityList: List<CryptoFavEntity> = listOf(
            CryptoFavEntity("BTC", "Bitcoin", "https://example.com/btc.png", System.currentTimeMillis()),
            CryptoFavEntity("ETH", "Ethereum", "https://example.com/eth.png", System.currentTimeMillis())
        )

        coEvery { coinRepository.getAllCoins() } throws Exception("Error!")
        coEvery { coinRepository.getAllFavoriteCoins() } returns flowOf(fakeCryptoFavEntityList)

        val getAllCoinsUseCase = GetAllCoinsUseCase(coinRepository)

        getAllCoinsUseCase().test {
            awaitError()
            assertThat(awaitError().message).isEqualTo("Error!")
        }
    }
}