package com.fkexample.cointicker.usecases

import app.cash.turbine.test
import com.fkexample.cointicker.cache.models.CryptoAssetEntity
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.repo.CoinRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
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

        val fakeCryptoFavEntityList = listOf(CryptoAssetEntity("BTC"), CryptoAssetEntity("ETH"))

        coEvery { coinRepository.getAllCoins() } returns flowOf(fakeCryptoEntityList)
        fakeCryptoFavEntityList.forEach {
            coEvery { coinRepository.addOrRemoveFavCoin(it) } just Runs
        }

        val getAllCoinsUseCase = GetAllCoinsUseCase(coinRepository)

        getAllCoinsUseCase().test {
            assertThat(awaitItem().loading).isTrue()
            assertThat(awaitItem().data?.size).isEqualTo(2)
            awaitComplete()
        }
    }

    @Test
    fun `invoke should emit error state on throwing exception`() = runTest {

        val fakeCryptoFavEntityList = listOf(CryptoAssetEntity("BTC"), CryptoAssetEntity("ETH"))

        coEvery { coinRepository.getAllCoins() } throws Exception("Error!")
        fakeCryptoFavEntityList.forEach {
            coEvery { coinRepository.addOrRemoveFavCoin(it) } just Runs
        }
        val getAllCoinsUseCase = GetAllCoinsUseCase(coinRepository)

        getAllCoinsUseCase().test {
            awaitError()
            assertThat(awaitError().message).isEqualTo("Error!")
        }
    }
}