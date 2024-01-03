package cz.mendelu.pef.flashyflashcards.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.mendelu.pef.flashyflashcards.model.AppPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    private val context: Context,
    private val gson: Gson
) : DataStoreRepository {
    override suspend fun setOnboardingFinished() {
        val onBoardingKey = booleanPreferencesKey(DataStoreConstants.ONBOARDING_FINISHED)

        context.dataStore.edit { preferences ->
            if (!preferences.contains(onBoardingKey)) {
                preferences[onBoardingKey] = true
            }
        }
    }

    override suspend fun updateAppPreferences(appPreferences: List<AppPreference>) {
        val appPreferencesKey = stringPreferencesKey(DataStoreConstants.APP_PREFERENCES)
        val type = object : TypeToken<List<AppPreference>>() {}.type
        val json = gson.toJson(appPreferences, type)

        context.dataStore.edit { preferences ->
            preferences[appPreferencesKey] = json
        }
    }

    override suspend fun getAppPreferences(): Flow<List<AppPreference>> {
        val appPreferencesKey = stringPreferencesKey(DataStoreConstants.APP_PREFERENCES)
        val type = object : TypeToken<List<AppPreference>>() {}.type

        return context.dataStore.data.map { preferences ->
            val appPreferencesString = preferences[appPreferencesKey] ?: "[]"

            gson.fromJson(appPreferencesString, type)
        }
    }

    override fun isOnboardingFinished(): Flow<Boolean> {
        val onBoardingKey = booleanPreferencesKey(DataStoreConstants.ONBOARDING_FINISHED)

        return context.dataStore.data.map { preferences ->
            preferences[onBoardingKey] ?: false
        }
    }

    override fun getTestAnswerLength(): Flow<Long> {
        val testAnswerLengthKey = longPreferencesKey(DataStoreConstants.TEST_ANSWER_LENGTH)

        return context.dataStore.data.map { preferences ->
            preferences[testAnswerLengthKey] ?: DataStoreConstants.DEFAULT_TEST_ANSWER_LENGTH_VALUE
        }
    }
}