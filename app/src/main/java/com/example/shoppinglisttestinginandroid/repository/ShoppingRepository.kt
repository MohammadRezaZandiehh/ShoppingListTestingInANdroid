package com.example.shoppinglisttestinginandroid.repository

import androidx.lifecycle.LiveData
import com.example.shoppinglisttestinginandroid.data.local.ShoppingItem
import com.example.shoppinglisttestinginandroid.data.responses.ImageResponse
import com.example.shoppinglisttestinginandroid.util.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}

/**
 * we want to pass both of "real repository" and "fake repository" to viewModel's constructor.
 * so we define a "repository interface" and sed it to view model until will be handled the both
 * repository*/