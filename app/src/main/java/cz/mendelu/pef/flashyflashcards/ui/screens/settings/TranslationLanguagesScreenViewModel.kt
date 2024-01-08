package cz.mendelu.pef.flashyflashcards.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.mlkit.MLKitTranslateManager
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TranslationLanguagesScreenViewModel @Inject constructor(
    private val mLKitTranslateManager: MLKitTranslateManager
) : BaseViewModel() {

    var uiState by mutableStateOf(UiState<Map<String, String>, ScreenErrors>(loading = true))

    init {
        getAllDownloadedLanguageCodesToNames()
    }

    fun deleteTranslationModel(code: String) {
        val oldData = uiState.data

        uiState = UiState(
            loading = true,
            data = oldData
        )

        mLKitTranslateManager.deleteTranslateModel(
            code = code,
            onSuccessDelete = {
                val newData = uiState.data?.filter { it.key != code }

                uiState = UiState(data = newData)
            },
            onFailureDelete = {
                uiState = UiState(
                    data = oldData,
                    errors = ScreenErrors(
                        messageRes = R.string.failed_to_delete_downloaded_language
                    )
                )
            }
        )
    }

    private fun getAllDownloadedLanguageCodesToNames() {
        mLKitTranslateManager.getDownloadedCodesToLanguages(
            onDownloadSuccess = {
                uiState = UiState(data = it)
            },
            onDownloadFailure = {
                uiState = UiState(
                    errors = ScreenErrors(
                        imageRes = R.drawable.undraw_warning,
                        messageRes = R.string.failed_to_get_downloaded_languages
                    )
                )
            }
        )
    }
}