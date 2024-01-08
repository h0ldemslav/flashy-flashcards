package cz.mendelu.pef.flashyflashcards.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
        getAllCollectionLanguages()
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
                // TODO
            }
        )
    }

    private fun getAllCollectionLanguages() {
        mLKitTranslateManager.getDownloadedCodesToLanguages(
            onDownloadSuccess = {
                uiState = UiState(data = it)
            },
            onDownloadFailure = {
                // TODO
            }
        )
    }
}