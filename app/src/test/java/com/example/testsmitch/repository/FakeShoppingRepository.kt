package com.example.testsmitch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testsmitch.data.local.ShoppingItem
import com.example.testsmitch.data.remote.responses.ImageResponse
import com.example.testsmitch.other.Resource


class FakeShoppingRepository : ShoppingRepository {

    private val shoppingItems  = mutableListOf<ShoppingItem>()
    private val observeShoppingItem = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observeTotalPrice = MutableLiveData<Float>()
    private var shouldNetworkError = false


    fun setNetworkError(value: Boolean){
        shouldNetworkError = value
    }

    fun updateItems(){
        observeShoppingItem.postValue(shoppingItems)
        observeTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumByDouble { it.price.toDouble() }.toFloat()
    }

    override suspend fun insertItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        updateItems()
    }

    override suspend fun deleteItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        updateItems()
    }

    override fun observeAllItems(): LiveData<List<ShoppingItem>> {
        return observeShoppingItem
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observeTotalPrice
    }

    override suspend fun searchImage(imgQuery: String): Resource<ImageResponse> {
        return if (shouldNetworkError){
            Resource.error("error",null)
        } else {
            Resource.success(ImageResponse(listOf(),0,0))
        }
    }
}