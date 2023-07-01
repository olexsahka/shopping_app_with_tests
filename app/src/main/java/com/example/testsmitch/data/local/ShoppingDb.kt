package com.example.testsmitch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [ShoppingItem::class])
abstract class ShoppingDb: RoomDatabase() {
    abstract fun shoppingDao() : ShoppingDao
}