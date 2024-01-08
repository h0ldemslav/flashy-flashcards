package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepository
import cz.mendelu.pef.flashyflashcards.mlkit.MLKitTranslateManager
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWordCollectionScreenViewModel @Inject constructor(
    private val wordCollectionsRepository: WordCollectionsRepository,
    private val mLKitTranslateManager: MLKitTranslateManager
) : BaseViewModel(), AddEditWordCollectionScreenActions {

    var uiState by mutableStateOf(UiState<WordCollection, ScreenErrors>(
        // Initial data is necessary!
        data = WordCollection(
            name = "",
            sourceLanguage = "",
            targetLanguage = ""
        )
    ))

    override fun saveWordCollection(wordCollection: WordCollection) {
        val (sourceLanguageCode, targetLanguageCode) = mLKitTranslateManager.getCurrentSourceAndTargetLanguageCodes()
        val languages = mLKitTranslateManager.getSourceAndTargetLanguageNames(sourceLanguageCode, targetLanguageCode)

        if (wordCollection.sourceLanguage != languages?.first ||
            wordCollection.targetLanguage != languages.second) {
                // User can change collection languages
                // Release translator in order to download model, when user attempts to translate
                // word according to fresh languages
                mLKitTranslateManager.closeTranslator()
                mLKitTranslateManager.resetTranslator()

                mLKitTranslateManager.setSourceAndTargetLanguageCodes(
                    wordCollection.sourceLanguage,
                    wordCollection.targetLanguage
                )
        }

        launch {
            if (wordCollection.id != null) {
                wordCollectionsRepository.updateWordCollection(wordCollection)
            } else {
                wordCollectionsRepository.createNewWordCollection(wordCollection)
            }
        }
    }

    override fun deleteWordCollection(wordCollection: WordCollection) {
        launch {
            wordCollectionsRepository.deleteWordCollection(wordCollection)
        }
    }

    override fun isWordCollectionValid(wordCollection: WordCollection): Boolean {
        val isNameEmpty = wordCollection.name.isEmpty()
        val languagesEitherEmptyOrTheSame = wordCollection.sourceLanguage.isEmpty() ||
                wordCollection.targetLanguage.isEmpty() ||
                wordCollection.sourceLanguage == wordCollection.targetLanguage

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
            data = wordCollection,
            errors = errors
        )

        return errors == null
    }

    override fun getAllLanguages(): Map<String, String> {
        return mLKitTranslateManager.getAllAvailableCodesToLanguages()
    }

    override fun setWordCollection(wordCollection: WordCollection?) {
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

    fun getWordCollectionById(id: Long?) {
        launch {
            wordCollectionsRepository.getWordCollectionById(id).collect { entity ->
                if (entity != null) {
                    val wordCollection = WordCollection.createFromEntity(entity)
                    setWordCollection(wordCollection)
                }
            }
        }
    }
}