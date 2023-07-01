package com.example.testsmitch.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testsmitch.MainCoroutinesRule
import com.example.testsmitch.getOrAwaitValue
import com.example.testsmitch.other.Constants
import com.example.testsmitch.other.Status
import com.example.testsmitch.repository.DefaultShoppingRepository
import com.example.testsmitch.repository.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest {


    private lateinit var viewModel: ShoppingViewModel

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutinesRule = MainCoroutinesRule()

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `insert shopping item with empty field return error`(){
        viewModel.addItem("name","","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with long name return error`(){
        val string = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH+1){
                append(1)
            }
        }
        viewModel.addItem(string,"5","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }



    @Test
    fun `insert shopping item with long price return error`(){
        val string = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH+1){
                append(1)
            }
        }
        viewModel.addItem("name","5",string)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with long amount return error`(){
        viewModel.addItem("name","111111111111111111111111111111111","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with return success`(){
        viewModel.addItem("name","11","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }


}