package cz.mendelu.pef.flashyflashcards.di

import cz.mendelu.pef.flashyflashcards.remote.YelpAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun provideYelpAPI(retrofit: Retrofit): YelpAPI = retrofit.create(YelpAPI::class.java)
}