package cz.mendelu.pef.flashyflashcards.di

import cz.mendelu.pef.flashyflashcards.remote.YelpAPI
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepository
import cz.mendelu.pef.flashyflashcards.remote.YelpAPIRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideYelpAPIRepository(yelpAPI: YelpAPI): YelpAPIRepository =
        YelpAPIRepositoryImpl(yelpAPI)
}