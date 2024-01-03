package cz.mendelu.pef.flashyflashcards.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.model.AppPreference
import cz.mendelu.pef.flashyflashcards.model.AppPreferenceConstants
import cz.mendelu.pef.flashyflashcards.model.AppPreferenceValue
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : BaseViewModel(), SettingsScreenActions {

    var uiState by mutableStateOf(UiState<List<AppPreference>, ScreenErrors>(
            loading = true,
            data = listOf(
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
                        value = AppPreferenceConstants.TEST_ANSWER_LENGTH_15_MIN,
                    )
            )
        )
    )

    private val preferencesValues = listOf(
        AppPreferenceValue(
            appPreferenceName = AppPreferenceConstants.LANG,
            displayName = R.string.language_en,
            name = AppPreferenceConstants.LANG_EN
        ),
        AppPreferenceValue(
            appPreferenceName = AppPreferenceConstants.LANG,
            displayName = R.string.language_cs,
            name = AppPreferenceConstants.LANG_CS
        ),
        AppPreferenceValue(
            appPreferenceName = AppPreferenceConstants.THEME,
            displayName = R.string.theme_light,
            name = AppPreferenceConstants.THEME_LIGHT
        ),
        AppPreferenceValue(
            appPreferenceName = AppPreferenceConstants.THEME,
            displayName = R.string.theme_dark,
            name = AppPreferenceConstants.THEME_DARK
        ),
        AppPreferenceValue(
            appPreferenceName = AppPreferenceConstants.TEST_ANSWER_LENGTH,
            displayName = R.string.test_length_5,
            name = AppPreferenceConstants.TEST_ANSWER_LENGTH_5_MIN
        ),
        AppPreferenceValue(
            appPreferenceName = AppPreferenceConstants.TEST_ANSWER_LENGTH,
            displayName = R.string.test_length_10,
            name = AppPreferenceConstants.TEST_ANSWER_LENGTH_10_MIN
        ),
        AppPreferenceValue(
            appPreferenceName = AppPreferenceConstants.TEST_ANSWER_LENGTH,
            displayName = R.string.test_length_15,
            name = AppPreferenceConstants.TEST_ANSWER_LENGTH_15_MIN
        ),
        AppPreferenceValue(
            appPreferenceName = AppPreferenceConstants.TEST_ANSWER_LENGTH,
            displayName = R.string.test_length_20,
            name = AppPreferenceConstants.TEST_ANSWER_LENGTH_20_MIN
        ),

    )

    init {
        fetchStoredAppPreferences()
    }

    override fun updateAppPreference(displayName: Int, displayValue: Int) {
        uiState.data?.let { preferences ->
            for (p in preferences) {
                if (p.displayName == displayName) {
                    p.displayValue = displayValue
                    p.value = preferencesValues.find { it.displayName == displayValue }?.name ?: ""

                    saveAppPreferences()

                    break
                }
            }
        }
    }

    override fun getAllAppPreferenceValues(appPreference: AppPreference): List<AppPreferenceValue> {
        return preferencesValues.filter { it.appPreferenceName == appPreference.name }
    }

    private fun fetchStoredAppPreferences() {
        launch {
            dataStoreRepository.getAppPreferences().collect { appPreferences ->
                val oldData = uiState.data

                uiState = UiState(data = appPreferences.ifEmpty { oldData })
            }
        }
    }

    private fun saveAppPreferences() {
        uiState.data?.let { data ->
            uiState = UiState(
                loading = true,
                data = data
            )

            launch {
                dataStoreRepository.updateAppPreferences(data)
                uiState = UiState(data = data)
            }
        }
    }
}