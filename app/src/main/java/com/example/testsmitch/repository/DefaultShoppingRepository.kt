package com.example.testsmitch.repository

import androidx.lifecycle.LiveData
import com.example.testsmitch.data.local.ShoppingDao
import com.example.testsmitch.data.local.ShoppingItem
import com.example.testsmitch.data.remote.PixabayAPI
import com.example.testsmitch.data.remote.responses.ImageResponse
import com.example.testsmitch.other.Resource
import retrofit2.Response
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
): ShoppingRepository {
    override suspend fun insertItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertItem(shoppingItem)
    }

    override suspend fun deleteItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteIte(shoppingItem)
    }

    override fun observeAllItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchImage(imgQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imgQuery)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                }?: Resource.error("error unknown",null)
            }
            else
                Resource.error("unknown Error",null)
        }
        catch (e: Exception){
            Resource.error("error reach server",null)
        }

    }

}