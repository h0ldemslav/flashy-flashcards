package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepository
import cz.mendelu.pef.flashyflashcards.mlkit.MLKitTranslator
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWordCollectionScreenViewModel @Inject constructor(
    private val wordCollectionsRepository: WordCollectionsRepository,
    private val mLKitTranslator: MLKitTranslator
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
        launch {
            mLKitTranslator.setLanguages(
                wordCollection.sourceLanguage,
                wordCollection.targetLanguage
            )

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
        return mLKitTranslator.getMapOfLanguages()
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