package cz.mendelu.pef.flashyflashcards.di

import com.google.gson.Gson
import cz.mendelu.pef.flashyflashcards.FlashyFlashcardsApplication
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(gson: Gson): DataStoreRepository =
        DataStoreRepositoryImpl(FlashyFlashcardsApplication.appContext, gson)
}
