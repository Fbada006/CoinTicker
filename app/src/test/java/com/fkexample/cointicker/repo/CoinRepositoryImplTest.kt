package com.fkexample.cointicker.repo

import app.cash.turbine.test
import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.network.TickerService
import com.fkexample.cointicker.network.models.CryptoNetwork
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CoinRepositoryImplTest {

    private val tickerService: TickerService = mockk()
    private val coinDao: CoinDao = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val coinRepository: CoinRepository = CoinRepositoryImpl(tickerService, coinDao, UnconfinedTestDispatcher())

    @Test
    fun `getAllCoins should emit non empty list`() = runTest {
        val mockCoins = listOf(
            CryptoNetwork(assetId = "BTC", name = "Bitcoin", iconId = null),
            CryptoNetwork(assetId = "ETH", name = "Ethereum", iconId = null),
            CryptoNetwork(assetId = "XRP", name = "Ripple", iconId = null)
        )
        val cryptoEntities = listOf(
            CryptoEntity(assetId = "BTC", name = "Bitcoin", iconId = "https://example.com/btc.png", dateCached = 1627513200000),
            CryptoEntity(assetId = "ETH", name = "Ethereum", iconId = "https://example.com/eth.png", dateCached = 1627513200000),
            CryptoEntity(assetId = "XRP", name = "Ripple", iconId = "https://example.com/xrp.png", dateCached = 1627513200000)
        )


        coEvery { tickerService.getAllCoins() } returns mockCoins
        coEvery { coinDao.getAllCoins() } returns flowOf(cryptoEntities)

        coinRepository.getAllCoins().test {
            assertThat(awaitItem().size).isEqualTo(3)
            awaitComplete()
        }
    }

    @Test
    fun `getAllCoins should emit non empty list even with network exception`() = runTest {
        val cryptoEntities = listOf(
            CryptoEntity(assetId = "BTC", name = "Bitcoin", iconId = "https://example.com/btc.png", dateCached = 1627513200000),
            CryptoEntity(assetId = "ETH", name = "Ethereum", iconId = "https://example.com/eth.png", dateCached = 1627513200000),
            CryptoEntity(assetId = "XRP", name = "Ripple", iconId = "https://example.com/xrp.png", dateCached = 1627513200000)
        )

        coEvery { tickerService.getAllCoins() } throws Exception()
        coEvery { coinDao.getAllCoins() } returns flowOf(cryptoEntities)

        coinRepository.getAllCoins().test {
            assertThat(awaitItem().size).isEqualTo(3)
            awaitComplete()
        }
    }
}
