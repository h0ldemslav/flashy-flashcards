package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepository
import cz.mendelu.pef.flashyflashcards.mlkit.Translator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWordCollectionScreenViewModel @Inject constructor(
    private val translator: Translator,
    private val wordCollectionsRepository: WordCollectionsRepository,
) : BaseViewModel(), AddEditWordCollectionScreenActions {

    var screenData by mutableStateOf(AddEditWordCollectionScreenData())

    fun saveWordCollection() {
        launch {
            wordCollectionsRepository.createNewWordCollection(screenData.wordCollection)
        }
    }

    fun validateScreenData() {
        val isNameEmpty = screenData.wordCollection.name.isEmpty()
        val languagesEitherEmptyOrTheSame = screenData.wordCollection.sourceLanguage.isEmpty() ||
                screenData.wordCollection.targetLanguage.isEmpty() ||
                screenData.wordCollection.sourceLanguage == screenData.wordCollection.targetLanguage

        screenData = if (isNameEmpty) {
            screenData.copy(wordCollectionError = R.string.word_collections_collection_error)
        } else if (languagesEitherEmptyOrTheSame) {
            screenData.copy(wordCollectionError = R.string.word_collections_languages_error)
        } else {
            screenData.copy(wordCollectionError = null)
        }
    }

    override fun getAllLanguages(): Map<String, String> {
        return translator.getListOfFullLanguageNames()
    }
}