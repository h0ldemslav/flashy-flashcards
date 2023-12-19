package cz.mendelu.pef.flashyflashcards.di

import cz.mendelu.pef.flashyflashcards.mlkit.MLKitTranslateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MLKitModule {

    @Provides
    @Singleton
    fun provideTranslator(): MLKitTranslateManager = MLKitTranslateManager()
}