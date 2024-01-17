package cz.mendelu.pef.flashyflashcards

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.gson.Gson
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreConstants
import cz.mendelu.pef.flashyflashcards.fake.FakeDataStoreRepositoryImpl
import cz.mendelu.pef.flashyflashcards.model.AppPreference
import cz.mendelu.pef.flashyflashcards.model.AppPreferenceConstants
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DataStoreRepositoryTest {

    private val testContext = ApplicationProvider.getApplicationContext<Context>()
    private val testDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testDispatcher + Job())
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testCoroutineScope,
        produceFile = {
            testContext.preferencesDataStoreFile("test_datastore")
        }
    )
    private val gson = Gson()
    private val repository = FakeDataStoreRepositoryImpl(gson, dataStore)

    private val appPreferences = listOf(
        AppPreference(
            displayName = R.string.app_language,
            displayValue = R.string.language_en,
            name = AppPreferenceConstants.LANG,
            value = AppPreferenceConstants.LANG_EN
        ),
        AppPreference(
            displayName = R.string.app_theme,
            displayValue = R.string.theme_light,
            name = AppPreferenceConstants.THEME,
            value = AppPreferenceConstants.THEME_LIGHT
        ),
        AppPreference(
            displayName = R.string.test_answer_length,
            displayValue = R.string.test_length_15,
            name = AppPreferenceConstants.TEST_ANSWER_LENGTH,
            value = AppPreferenceConstants.TEST_ANSWER_LENGTH_15_SEC,
        )
    )

    @Test
    fun testAddAndGetAppPreferences() = testCoroutineScope.runTest {
        val appPreferencesCopy = appPreferences.map { it.copy() }

        repository.updateAppPreferences(appPreferencesCopy)
        repository.getAppPreferences().collect { preferences ->
            assert(preferences.size == appPreferencesCopy.size)

            for (index in preferences.indices) {
                assert(preferences[index] == appPreferencesCopy[index])
            }
        }
    }

    @Test
    fun testAnswerLength() = testCoroutineScope.runTest {
        repository.getTestAnswerLength().collect {
            assert(it == DataStoreConstants.DEFAULT_TEST_ANSWER_LENGTH_VALUE)
        }

        val newAnswerLength = AppPreferenceConstants.TEST_ANSWER_LENGTH_10_SEC.toLongOrNull()
        assert(newAnswerLength != null)

        repository.setTestAnswerLength(newAnswerLength!!)
        repository.getTestAnswerLength().collect {
            assert(it == newAnswerLength)
        }
    }

    @Test
    fun testOnboardingStatus() = testCoroutineScope.runTest {
        repository.isOnboardingFinished().collect {
            assert(!it)
        }
        repository.setOnboardingFinished()
        repository.isOnboardingFinished().collect {
            assert(it)
        }
    }
}