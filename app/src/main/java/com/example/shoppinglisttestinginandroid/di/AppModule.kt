package com.example.shoppinglisttestinginandroid.di

import android.content.Context
import androidx.room.Room
import com.example.shoppinglisttestinginandroid.data.local.ShoppingDao
import com.example.shoppinglisttestinginandroid.util.Constants.BASE_URL
import com.example.shoppinglisttestinginandroid.util.Constants.DATABASE_NAME
import com.example.shoppinglisttestinginandroid.data.local.ShoppingItemDatabase
import com.example.shoppinglisttestinginandroid.data.remote.PixabayAPI
import com.example.shoppinglisttestinginandroid.repository.DefaultShoppingRepository
import com.example.shoppinglisttestinginandroid.repository.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)                                                             //just make sure that the lifetime of the dependencies we declear here in this module will be as long as our application life.
object AppModule {

    @Singleton
    @Provides                                                                                       // hey dagger we provide this module (room database) here.
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(                                                           // if we didn't want to send repository to viewModel we don't need to write this function.
        dao: ShoppingDao,                                                                           //because in repository class we pass "dao and api" in constructor and dagger knew it and handle it bu itself.
        api: PixabayAPI
    ) = DefaultShoppingRepository(dao, api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }
}