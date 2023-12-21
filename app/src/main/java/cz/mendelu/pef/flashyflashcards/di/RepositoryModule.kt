package cz.mendelu.pef.flashyflashcards.di

import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesDao
import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepositoryImpl
import cz.mendelu.pef.flashyflashcards.database.businesses.BusinessesRepository
import cz.mendelu.pef.flashyflashcards.database.wordcollections.TestHistoryDao
import cz.mendelu.pef.flashyflashcards.database.wordcollections.TestHistoryRepository
import cz.mendelu.pef.flashyflashcards.database.wordcollections.TestHistoryRepositoryImpl
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsDao
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepository
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepositoryImpl
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordsDao
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordsRepository
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordsRepositoryImpl
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

    // Remote
    //
    @Provides
    @Singleton
    fun provideYelpAPIRepository(yelpAPI: YelpAPI): YelpAPIRepository =
        YelpAPIRepositoryImpl(yelpAPI)

    // Local
    //
    @Provides
    @Singleton
    fun provideWordCollectionsRepository(dao: WordCollectionsDao): WordCollectionsRepository =
        WordCollectionsRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideWordsRepository(dao: WordsDao): WordsRepository =
        WordsRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideTestHistoryRepository(dao: TestHistoryDao): TestHistoryRepository =
        TestHistoryRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideBusinessesRepository(dao: BusinessesDao): BusinessesRepository =
        BusinessesRepositoryImpl(dao)
}