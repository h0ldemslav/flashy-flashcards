package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepository
import cz.mendelu.pef.flashyflashcards.extensions.getTitleCase
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashcardPracticeScreenViewModel @Inject constructor(
    private val wordCollectionsRepository: WordCollectionsRepository
) : BaseViewModel(), FlashcardPracticeScreenActions {

    var uiState by mutableStateOf(UiState<Word, ScreenErrors>())
    private var fetchedListOfWords: List<Word> = emptyList()
    private var currentWordIndex: Int = 0

    fun getAllCollectionWords(collectionId: Long?) {
        val errors = if (collectionId == null) {
            ScreenErrors(
                imageRes = null,
                messageRes = R.string.something_went_wrong_error
            )
        } else {
            null
        }

        uiState = UiState(
            loading = errors == null,
            errors = errors
        )

        if (errors == null) {
            launch {
                wordCollectionsRepository.getWordCollectionAndWordsById(collectionId)
                    .collect { entity ->
                        if (entity != null) {
                            fetchedListOfWords = entity.wordEntities
                                .map { Word.createFromWordEntity(it) }
                                .shuffled()

                            uiState = UiState(
                                data = fetchedListOfWords.firstOrNull()
                            )
                        }
                }
            }
        }
    }

    override fun isAnswerCorrect(answer: String): Boolean {
        val processedAnswer = answer
            .trim()
            .getTitleCase()

        val errors = if (processedAnswer != uiState.data?.translation) {
            ScreenErrors(
                imageRes = null,
                messageRes = R.string.incorrect_answer
            )
        } else {
            null
        }

        uiState = UiState(
            data = uiState.data,
            errors = errors
        )

        return errors == null
    }

    override fun setNextWord() {
        val next = currentWordIndex + 1

        if (next != fetchedListOfWords.size) {
            currentWordIndex = next

            uiState = UiState(
                data = fetchedListOfWords[currentWordIndex]
            )
        } else {
            // Practice finished
            uiState = UiState()
        }
    }

    override fun resetWordToTheFirst() {
        currentWordIndex = 0
        fetchedListOfWords = fetchedListOfWords.shuffled()

        uiState = UiState(
            data = fetchedListOfWords.firstOrNull()
        )
    }
}