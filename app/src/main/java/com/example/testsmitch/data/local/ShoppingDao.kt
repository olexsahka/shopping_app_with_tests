package com.example.testsmitch.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteIte(shoppingItem: ShoppingItem)

    @Query("SELECT *  FROM  SHOPPING_ITEMS")
    fun observeAllItems(): LiveData<List<ShoppingItem>>

    @Query("SELECT SUM( price*amount) FROM  SHOPPING_ITEMS")
    fun observeTotalPrice(): LiveData<Float>
}