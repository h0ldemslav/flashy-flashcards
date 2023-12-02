package cz.mendelu.pef.flashyflashcards.di

import cz.mendelu.pef.flashyflashcards.mlkit.Translator
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
    fun provideTranslator(): Translator = Translator()
}