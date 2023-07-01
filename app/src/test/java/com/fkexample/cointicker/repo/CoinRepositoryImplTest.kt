package com.fkexample.cointicker.repo

import app.cash.turbine.test
import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.network.TickerService
import com.fkexample.cointicker.network.models.CryptoImageNetwork
import com.fkexample.cointicker.network.models.CryptoNetwork
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
            CryptoNetwork(assetId = "BTC", name = "Bitcoin"),
            CryptoNetwork(assetId = "ETH", name = "Ethereum"),
            CryptoNetwork(assetId = "XRP", name = "Ripple")
        )
        val mockIcons = listOf(
            CryptoImageNetwork(assetId = "BTC", url = "https://example.com/btc.png"),
            CryptoImageNetwork(assetId = "ETH", url = "https://example.com/eth.png"),
            CryptoImageNetwork(assetId = "XRP", url = "https://example.com/xrp.png")
        )
        val cryptoEntities = listOf(
            CryptoEntity(assetId = "BTC", name = "Bitcoin", cryptoUrl = "https://example.com/btc.png", dateCached = 1627513200000),
            CryptoEntity(assetId = "ETH", name = "Ethereum", cryptoUrl = "https://example.com/eth.png", dateCached = 1627513200000),
            CryptoEntity(assetId = "XRP", name = "Ripple", cryptoUrl = "https://example.com/xrp.png", dateCached = 1627513200000)
        )


        coEvery { tickerService.getAllCoins() } returns mockCoins
        coEvery { tickerService.getAllIcons() } returns mockIcons
        coEvery { coinDao.getAllCoins() } returns cryptoEntities

        coinRepository.getAllCoins().test {
            assertThat(awaitItem().size).isEqualTo(3)
            awaitComplete()
        }
    }

    @Test
    fun `getAllCoins should emit non empty list even with network exception`() = runTest {
        val cryptoEntities = listOf(
            CryptoEntity(assetId = "BTC", name = "Bitcoin", cryptoUrl = "https://example.com/btc.png", dateCached = 1627513200000),
            CryptoEntity(assetId = "ETH", name = "Ethereum", cryptoUrl = "https://example.com/eth.png", dateCached = 1627513200000),
            CryptoEntity(assetId = "XRP", name = "Ripple", cryptoUrl = "https://example.com/xrp.png", dateCached = 1627513200000)
        )

        coEvery { tickerService.getAllCoins() } throws Exception()
        coEvery { tickerService.getAllIcons() } throws Exception()
        coEvery { coinDao.getAllCoins() } returns cryptoEntities

        coinRepository.getAllCoins().test {
            assertThat(awaitItem().size).isEqualTo(3)
            awaitComplete()
        }
    }
}
