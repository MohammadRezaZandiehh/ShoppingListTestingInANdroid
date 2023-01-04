package com.example.shoppinglisttestinginandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shoppinglisttestinginandroid.data.local.ShoppingDao
import com.example.shoppinglisttestinginandroid.data.local.ShoppingItem

@Database(
    entities = [ShoppingItem::class],
    version = 1
)
abstract class ShoppingItemDatabase : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingDao
}