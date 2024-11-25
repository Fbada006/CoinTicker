package com.fkexample.cointicker.cache.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoAssetEntity
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TickerAppDatabaseTest {

    private lateinit var dao: CoinDao
    private lateinit var db: TickerAppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, TickerAppDatabase::class.java
        ).build()
        dao = db.coinDao()
    }

    @Test
    fun `test adding empty list followed by valid data`() = runTest {
        dao.insertCoins(emptyList())

        assertThat(dao.getAllCoins().first()).isEmpty()

        dao.insertCoins(
            listOf(CryptoEntity(assetId = "luptatum", name = "Antonia Finley", iconId = null, dateCached = 5034))
        )

        assertThat(dao.getAllCoins().first().size).isEqualTo(1)
    }

    @Test
    fun `adding fav to db returns valid data on query`() = runTest {
        dao.insertCoins(
            listOf(
                CryptoEntity(assetId = "reprehendunt", name = "Antonia Finley", iconId = null, dateCached = 5034),
                CryptoEntity(assetId = "sem", name = "Antonia Finley", iconId = null, dateCached = 5034)
            )
        )

        val entity = CryptoAssetEntity(assetId = "reprehendunt")
        val anotherEntity = CryptoAssetEntity(assetId = "sem")

        dao.insertFavCoin(entity)
        dao.insertFavCoin(anotherEntity)

        assertThat(dao.getAllFavoriteCoins().first().size).isEqualTo(2)
        assertThat(dao.getFavById(anotherEntity.assetId)).isEqualTo(anotherEntity)
    }

    @Test
    fun `deleting fav to db updates properly`() = runTest {
        dao.insertCoins(
            listOf(
                CryptoEntity(assetId = "reprehendunt", name = "Antonia Finley", iconId = null, dateCached = 5034),
                CryptoEntity(assetId = "sem", name = "Antonia Finley", iconId = null, dateCached = 5034)
            )
        )

        val entity = CryptoAssetEntity(assetId = "reprehendunt")
        val anotherEntity = CryptoAssetEntity(assetId = "sem")
        dao.insertFavCoin(entity)
        dao.insertFavCoin(anotherEntity)

        assertThat(dao.getAllFavoriteCoins().first().size).isEqualTo(2)

        dao.deleteCoinFromFav(entity)
        assertThat(dao.getAllFavoriteCoins().first().size).isEqualTo(1)

        dao.deleteCoinFromFav(anotherEntity)
        assertThat(dao.getAllFavoriteCoins().first()).isEmpty()
    }
}