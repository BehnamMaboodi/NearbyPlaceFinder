package me.behna.nearbyplace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import me.behna.nearbyplace.data.repository.business.BusinessRepository
import me.behna.nearbyplace.domain.use_case.SearchForBusinessUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideSearchForBusinessUseCase(repository: BusinessRepository): SearchForBusinessUseCase {
        return SearchForBusinessUseCase(repository)
    }
}