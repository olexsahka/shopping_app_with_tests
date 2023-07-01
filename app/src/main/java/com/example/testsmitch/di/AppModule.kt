package com.example.testsmitch.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testsmitch.R
import com.example.testsmitch.data.local.ShoppingDao
import com.example.testsmitch.data.local.ShoppingDb
import com.example.testsmitch.data.remote.PixabayAPI
import com.example.testsmitch.repository.DefaultShoppingRepository
import com.example.testsmitch.repository.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,ShoppingDb::class.java,com.example.testsmitch.other.Constants.DATABASE_NAME).build()


    @Singleton
    @Provides
    fun provideDao(appDatabase: ShoppingDb) :ShoppingDao = appDatabase.shoppingDao()

    @Singleton
    @Provides
    fun providesPixabay(): PixabayAPI {
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(com.example.testsmitch.other.Constants.BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesRepository(
        dao: ShoppingDao,
        pixabayAPI: PixabayAPI
    ) = DefaultShoppingRepository(dao,pixabayAPI) as ShoppingRepository

    @Singleton
    @Provides
    fun providesGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_image_24)
    )
}