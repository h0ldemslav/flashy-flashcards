package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordsRepository
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWordScreenViewModel @Inject constructor(
    private val wordsRepository: WordsRepository
) : BaseViewModel(), AddEditWordScreenActions {

    var uiState by mutableStateOf(UiState<Word, ScreenErrors>(
        // Initial data is necessary!
        data = Word(
            name = "",
            translation = "",
            wordCollectionId = null
        )
    ))

    override fun isWordValid(word: Word): Boolean {
        val errors = if (word.name.isEmpty()) {
            ScreenErrors(
                imageRes = null,
                messageRes = R.string.words_name_error
            )
        } else if (word.translation.isEmpty()) {
            ScreenErrors(
                imageRes = null,
                messageRes = R.string.words_translation_error
            )
        } else {
            null
        }

        uiState = UiState(
            data = word,
            errors = errors
        )

        return errors == null
    }

    override fun setWord(word: Word?, collectionId: Long?) {
        var data = uiState.data

        if (word == null) {
            // New word
            data?.wordCollectionId = collectionId
        } else {
            // Existing word
            data = word.copy()
        }

        uiState = UiState(
            data = data
        )
    }

    override fun saveWord(word: Word) {
        launch {
            if (word.id != null) {
                wordsRepository.updateWord(word)
            } else {
                wordsRepository.addNewWord(word)
            }
        }
    }

    override fun deleteWord(word: Word) {
        launch {
            wordsRepository.deleteWord(word)
        }
    }
}