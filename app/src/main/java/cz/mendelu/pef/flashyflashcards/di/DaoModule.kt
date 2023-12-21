package cz.mendelu.pef.flashyflashcards.di

import cz.mendelu.pef.flashyflashcards.database.FlashyFlashcardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideWordCollectionsDao(flashyFlashcardsDatabase: FlashyFlashcardsDatabase) =
        flashyFlashcardsDatabase.wordCollectionsDao()

    @Provides
    @Singleton
    fun provideWordsDao(flashyFlashcardsDatabase: FlashyFlashcardsDatabase) =
        flashyFlashcardsDatabase.wordsDao()

    @Provides
    @Singleton
    fun provideTestHistoryDao(flashyFlashcardsDatabase: FlashyFlashcardsDatabase) =
        flashyFlashcardsDatabase.testHistoryDao()

    @Provides
    @Singleton
    fun provideBusinessesDao(flashyFlashcardsDatabase: FlashyFlashcardsDatabase) =
        flashyFlashcardsDatabase.businessesDao()
}