package com.fkexample.cointicker.usecases

import app.cash.turbine.test
import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.network.TickerService
import com.fkexample.cointicker.network.models.CryptoDataNetwork
import com.fkexample.cointicker.network.models.CryptoReferenceNetwork
import com.fkexample.cointicker.repo.CoinRepository
import com.fkexample.cointicker.repo.CoinRepositoryImpl
import com.fkexample.cointicker.utils.EURO_SYMBOL
import com.fkexample.cointicker.utils.GBP_SYMBOL
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetCoinDetailsUseCaseTest {

    private val tickerService: TickerService = mockk()
    private val dao: CoinDao = mockk()
    private val coinRepository = CoinRepositoryImpl(tickerService, dao)

    @Test
    fun `invoke with valid data returns success`() = runTest {
        coEvery { tickerService.getCoinDetails("assetId") } returns listOf(
            CryptoDataNetwork(assetId = "assetId", priceUsd = 12.0)
        )

        coEvery { tickerService.getCoinExchangeRate(EURO_SYMBOL, "assetId") } returns CryptoReferenceNetwork("base", "assetId", 45.0)
        coEvery { tickerService.getCoinExchangeRate(GBP_SYMBOL, "assetId") } returns CryptoReferenceNetwork("base", "assetId", 45.0)
        coEvery { dao.getCoinById("assetId") } returns CryptoEntity(
            assetId = "assetId",
            name = "Daryl Shelton",
            cryptoUrl = "hello",
            dateCached = 7294
        )

        val getCoinDetailsUseCase = GetCoinDetailsUseCase(coinRepository)

        getCoinDetailsUseCase.invoke("assetId").test {
            assertThat(awaitItem().loading).isTrue()
            assertThat(awaitItem().data?.name).isEqualTo("Daryl Shelton")
            awaitComplete()
        }
    }

    @Test
    fun `invoke with null data returns null values`() = runTest {
        coEvery { tickerService.getCoinDetails("assetId") } returns listOf(
            CryptoDataNetwork(assetId = "assetId", priceUsd = 12.0)
        )

        coEvery { tickerService.getCoinExchangeRate(EURO_SYMBOL, "assetId") } returns CryptoReferenceNetwork("base", "assetId", 45.0)
        coEvery { tickerService.getCoinExchangeRate(GBP_SYMBOL, "assetId") } returns CryptoReferenceNetwork("base", "assetId", 45.0)
        coEvery { dao.getCoinById("assetId") } returns null

        val getCoinDetailsUseCase = GetCoinDetailsUseCase(coinRepository)

        getCoinDetailsUseCase.invoke("assetId").test {
            assertThat(awaitItem().loading).isTrue()
            assertThat(awaitItem().data?.name).isNull()
            awaitComplete()
        }
    }

    @Test
    fun `invoke with null and exception returns null values`() = runTest {
        coEvery { tickerService.getCoinDetails("assetId") } throws Exception()

        coEvery { tickerService.getCoinExchangeRate(EURO_SYMBOL, "assetId") } returns CryptoReferenceNetwork("base", "assetId", 45.0)
        coEvery { tickerService.getCoinExchangeRate(GBP_SYMBOL, "assetId") } returns CryptoReferenceNetwork("base", "assetId", 45.0)
        coEvery { dao.getCoinById("assetId") } returns null

        val getCoinDetailsUseCase = GetCoinDetailsUseCase(coinRepository)

        getCoinDetailsUseCase.invoke("assetId").test {
            assertThat(awaitItem().loading).isTrue()
            assertThat(awaitItem().data?.name).isNull()
            awaitComplete()
        }
    }

    @Test
    fun `invoke repo with exception returns error`() = runTest {
        val coinRepository: CoinRepository = mockk()
        coEvery { coinRepository.getCoinDetails("") } throws Exception("Error!")

        val getCoinDetailsUseCase = GetCoinDetailsUseCase(coinRepository)

        getCoinDetailsUseCase.invoke("").test {
            awaitError()
            assertThat(awaitError().message).isEqualTo("Error!")
        }
    }
}