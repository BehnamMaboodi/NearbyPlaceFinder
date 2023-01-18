package me.behna.nearbyplace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.behna.nearbyplace.api.RetrofitInstance
import me.behna.nearbyplace.api.YelpApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Provides
    @Singleton
    fun provideApiService(): YelpApiService {
        return RetrofitInstance.api
    }

}