package cz.mendelu.pef.flashyflashcards.di

import android.content.Context
import cz.mendelu.pef.flashyflashcards.database.FlashyFlashcardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): FlashyFlashcardsDatabase =
        FlashyFlashcardsDatabase.getDatabase(appContext)
}