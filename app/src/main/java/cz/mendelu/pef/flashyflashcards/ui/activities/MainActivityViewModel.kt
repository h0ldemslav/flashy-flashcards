package cz.mendelu.pef.flashyflashcards.ui.activities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.ramcosta.composedestinations.spec.NavGraphSpec
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.datastore.DataStoreRepository
import cz.mendelu.pef.flashyflashcards.model.AppPreferenceConstants
import cz.mendelu.pef.flashyflashcards.ui.screens.NavGraphs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : BaseViewModel() {

    val startNavGraph: MutableState<NavGraphSpec> = mutableStateOf(NavGraphs.root)
    val isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isDarkTheme: MutableState<Boolean> = mutableStateOf(false)
    val language: MutableState<String> = mutableStateOf(AppPreferenceConstants.LANG_EN)

    init {
        launch {
            dataStoreRepository.isOnboardingFinished()
                .distinctUntilChanged()
                .collect { onBoardingState ->
                    if (onBoardingState) {
                        startNavGraph.value = NavGraphs.collections
                    }
            }
        }

        launch {
            dataStoreRepository.getAppPreferences().collect { preferences ->
                val theme = preferences.find { it.name == AppPreferenceConstants.THEME }
                val languagePreference = preferences.find { it.name == AppPreferenceConstants.LANG }

                language.value = languagePreference?.value ?: AppPreferenceConstants.LANG_EN
                isDarkTheme.value = theme?.value == AppPreferenceConstants.THEME_DARK
                isLoading.value = false
            }
        }
    }
}