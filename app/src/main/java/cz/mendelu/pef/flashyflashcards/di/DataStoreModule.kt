package cz.mendelu.pef.flashyflashcards.di

import android.content.Context
import com.google.gson.Gson
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext appContext: Context, gson: Gson): DataStoreRepository =
        DataStoreRepositoryImpl(appContext, gson)
}
