package com.example.shoppinglisttestinginandroid.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.shoppinglisttestinginandroid.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)                  //inja mohite aslie java ya kotlin ya har zabani k rooye JVM run mishe nist --> inja mohite android e pas az in annotation use mikonim(rooye emulator run mishe na roye jvm) (instrumented test).
@SmallTest                                      // inja unit test e hamoon mosalase dar ghesmate aval e video ha --> "behtare" in @ ro bezarim
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setUp() {
        //inMemoryDatabaseBuilder() --> not a real database. age vase har database ye databaseBuilder bezanim (vase har test) ham sangin mishe ham alaki hafeze por mishe.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()
        /** allowMainThreadQueries(): ma etefaghan mikhaym dar test case hamon multi thread nabashe --> chon "many threads manipulated each other"
         * because we can not predict how the threads run at the same time
         */
        dao = database.shoppingDao()
    }

    /**
     * dar test case ha ma niazy b concurrency (ham zamani) nadarim --> az runBlocking k dar main thread anjam mishe use mikonim*/


    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)

        /** tooye delete aval bayad ye bar add konim bad delete konim.*/
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 2, 10f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 4, 5.5f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 0, 100f, "url", id = 3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)
    }
}

/** runBlocking --> run coroutine but in the "MAIN THREAD"*/


/*5*/
/** observeAllShoppingItems() --> return a livedata
 * hamonjor k midonim lave data async e va ma dar test case ha in o nmikhaym --> google ye class i erae dade k okesh mikone.*/


/**
 * masalan function e insert dat dao chon suspend fun ast --> dar runBlocking bas run she*/