package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepository
import cz.mendelu.pef.flashyflashcards.mlkit.Translator
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWordCollectionScreenViewModel @Inject constructor(
    private val translator: Translator,
    private val wordCollectionsRepository: WordCollectionsRepository,
) : BaseViewModel(), AddEditWordCollectionScreenActions {

    var uiState by mutableStateOf(UiState<WordCollection, ScreenErrors>())

    fun saveWordCollection() {
        if (uiState.data != null) {
            launch {
                wordCollectionsRepository.createNewWordCollection(uiState.data!!)
            }
        }
    }

    fun validateScreenData() {
        uiState.data?.let { col ->
            val isNameEmpty = col.name.isEmpty()
            val languagesEitherEmptyOrTheSame = col.sourceLanguage.isEmpty() ||
                    col.targetLanguage.isEmpty() || col.sourceLanguage == col.targetLanguage

            val errors = if (isNameEmpty) {
                ScreenErrors(
                    imageRes = null,
                    messageRes = R.string.word_collections_collection_error
                )
            } else if (languagesEitherEmptyOrTheSame) {
                ScreenErrors(
                    imageRes = null,
                    messageRes = R.string.word_collections_languages_error
                )
            } else {
                null
            }

            uiState = UiState(
                data = col,
                errors = errors
            )
        }
    }

    override fun getAllLanguages(): Map<String, String> {
        return translator.getListOfFullLanguageNames()
    }

    override fun createOrUpdateWordCollection(wordCollection: WordCollection?) {
        val errors = uiState.errors

        val data = WordCollection(
            id = wordCollection?.id,
            name = wordCollection?.name ?: "",
            sourceLanguage = wordCollection?.sourceLanguage ?: "",
            targetLanguage = wordCollection?.targetLanguage ?: ""
        )

        uiState = UiState(
            data = data,
            errors = errors
        )
    }
}