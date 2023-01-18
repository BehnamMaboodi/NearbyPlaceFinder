package me.behna.nearbyplace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import me.behna.nearbyplace.api.YelpApiService
import me.behna.nearbyplace.repository.business.BaseBusinessRepository
import me.behna.nearbyplace.repository.business.BusinessRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideBusinessRepository(api: YelpApiService): BaseBusinessRepository {
        return BusinessRepository(api)
    }
}