package cz.mendelu.pef.flashyflashcards.fake

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreConstants
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.model.AppPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeDataStoreRepositoryImpl(
    private val gson: Gson,
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {
    override fun isOnboardingFinished(): Flow<Boolean> {
        val onBoardingKey = booleanPreferencesKey(DataStoreConstants.ONBOARDING_FINISHED)

        return flow {
            val preferences = dataStore.data.first()
            emit(preferences[onBoardingKey] ?: false)
        }
    }

    override fun getTestAnswerLength(): Flow<Long> {
        val testAnswerLengthKey = longPreferencesKey(DataStoreConstants.TEST_ANSWER_LENGTH)

        return flow {
            val preferences = dataStore.data.first()
            emit(preferences[testAnswerLengthKey] ?: DataStoreConstants.DEFAULT_TEST_ANSWER_LENGTH_VALUE)
        }
    }

    override suspend fun setTestAnswerLength(length: Long) {
        val testAnswerLengthKey = longPreferencesKey(DataStoreConstants.TEST_ANSWER_LENGTH)

        dataStore.edit { preferences ->
            preferences[testAnswerLengthKey] = length
        }
    }

    override suspend fun setOnboardingFinished() {
        val onBoardingKey = booleanPreferencesKey(DataStoreConstants.ONBOARDING_FINISHED)

        dataStore.edit { preferences ->
            if (!preferences.contains(onBoardingKey)) {
                preferences[onBoardingKey] = true
            }
        }
    }

    override suspend fun updateAppPreferences(appPreferences: List<AppPreference>) {
        val appPreferencesKey = stringPreferencesKey(DataStoreConstants.APP_PREFERENCES)
        val type = object : TypeToken<List<AppPreference>>() {}.type
        val json = gson.toJson(appPreferences, type)

        dataStore.edit { preferences ->
            preferences[appPreferencesKey] = json
        }
    }

    override suspend fun getAppPreferences(): Flow<List<AppPreference>> {
        val appPreferencesKey = stringPreferencesKey(DataStoreConstants.APP_PREFERENCES)
        val type = object : TypeToken<List<AppPreference>>() {}.type

        return flowOf(dataStore.data.first())
            .map { preferences ->
                val appPreferencesString = preferences[appPreferencesKey] ?: "[]"
                gson.fromJson(appPreferencesString, type)
            }
    }
}