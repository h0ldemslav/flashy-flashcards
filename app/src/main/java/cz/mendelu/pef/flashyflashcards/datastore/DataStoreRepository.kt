package cz.mendelu.pef.flashyflashcards.datastore

import cz.mendelu.pef.flashyflashcards.model.AppPreference
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    fun isOnboardingFinished(): Flow<Boolean>
    fun getTestAnswerLength(): Flow<Long>
    suspend fun setOnboardingFinished()
    suspend fun updateAppPreferences(appPreferences: List<AppPreference>)
    suspend fun getAppPreferences(): Flow<List<AppPreference>>
}