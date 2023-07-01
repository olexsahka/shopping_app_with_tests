package com.example.testsmitch.repository

import androidx.lifecycle.LiveData
import com.example.testsmitch.data.local.ShoppingItem
import com.example.testsmitch.data.remote.responses.ImageResponse
import com.example.testsmitch.other.Resource

interface ShoppingRepository {
    suspend fun insertItem(shoppingItem: ShoppingItem)

    suspend fun deleteItem(shoppingItem: ShoppingItem)

    fun  observeAllItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice() : LiveData<Float>

    suspend fun searchImage(imgQuery: String) : Resource<ImageResponse>
}