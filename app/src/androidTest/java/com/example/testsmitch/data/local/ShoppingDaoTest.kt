package com.example.testsmitch.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.testsmitch.getOrAwaitValue
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @get:Rule
    var  instanceExecutionRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingDb
    private lateinit var dao : ShoppingDao


    @Before
    fun setup(){
        hiltRule.inject()
        dao = database.shoppingDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertItem() = runBlocking {
        val shoppingItem = ShoppingItem(
            name = "name",
            amount = 1,
            price = 1.0f,
            imageUrl = "asd",
            id = 1
        )
        dao.insertItem(shoppingItem)

        val allShoppingItem = dao.observeAllItems().getOrAwaitValue()
        Truth.assertThat(allShoppingItem).contains(shoppingItem)
    }

    @Test
    fun deleteItem() = runBlocking {
        val shoppingItem = ShoppingItem(
            name = "name",
            amount = 1,
            price = 1.0f,
            imageUrl = "asd",
            id = 1
        )
        dao.insertItem(shoppingItem)
        dao.deleteIte(shoppingItem)
        val allShoppingItem = dao.observeAllItems().getOrAwaitValue()

        Truth.assertThat(allShoppingItem).doesNotContain(shoppingItem)
    }


    @Test
    fun observeTotalPrice() = runBlocking {
        val shoppingItem1 = ShoppingItem(
            name = "name",
            amount = 3,
            price = 2.0f,
            imageUrl = "asd",
            id = 1
        )
        val shoppingItem2 = ShoppingItem(
            name = "name",
            amount = 13,
            price = 1.5f,
            imageUrl = "asd",
            id = 2
        )
        val shoppingItem3 = ShoppingItem(
            name = "name",
            amount = 0,
            price = 1222.0f,
            imageUrl = "asd",
            id = 3
        )
        dao.insertItem(shoppingItem1)
        dao.insertItem(shoppingItem2)
        dao.insertItem(shoppingItem3)
        val total = dao.observeTotalPrice().getOrAwaitValue()
        Truth.assertThat(total).isEqualTo(13*1.5f+3*2f)

    }


}