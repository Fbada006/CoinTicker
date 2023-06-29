package com.fkexample.cointicker.cache.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fkexample.cointicker.cache.CoinDao
import com.fkexample.cointicker.cache.models.CryptoEntity
import com.fkexample.cointicker.cache.models.CryptoFavEntity
import com.google.common.truth.Truth.*
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

        assertThat(dao.getAllCoins()).isEmpty()

        dao.insertCoins(
            listOf(CryptoEntity(assetId = "luptatum", name = "Antonia Finley", cryptoUrl = null, dateCached = 5034))
        )

        assertThat(dao.getAllCoins().size).isEqualTo(1)
    }

    @Test
    fun `adding fav to db returns valid data on query`() = runTest {
        val entity = CryptoFavEntity(assetId = "reprehendunt", name = "Lillie Franklin", cryptoUrl = null, dateCached = 4886)
        val anotherEntity = CryptoFavEntity(assetId = "sem", name = "Jeremiah Orr", cryptoUrl = null, dateCached = 3934)
        dao.insertFavCoin(entity)
        dao.insertFavCoin(anotherEntity)

        assertThat(dao.getAllFavoriteCoins().size).isEqualTo(2)
        assertThat(dao.getFavById(anotherEntity.assetId)?.dateCached).isEqualTo(anotherEntity.dateCached)
    }

    @Test
    fun `deleting fav to db updates properly`() = runTest {
        val entity = CryptoFavEntity(assetId = "reprehendunt", name = "Lillie Franklin", cryptoUrl = null, dateCached = 4886)
        val anotherEntity = CryptoFavEntity(assetId = "sem", name = "Jeremiah Orr", cryptoUrl = null, dateCached = 3934)
        dao.insertFavCoin(entity)
        dao.insertFavCoin(anotherEntity)

        assertThat(dao.getAllFavoriteCoins().size).isEqualTo(2)

        dao.deleteCoinFromFav(entity)
        assertThat(dao.getAllFavoriteCoins().size).isEqualTo(1)

        dao.deleteCoinFromFav(anotherEntity)
        assertThat(dao.getAllFavoriteCoins()).isEmpty()
    }
}