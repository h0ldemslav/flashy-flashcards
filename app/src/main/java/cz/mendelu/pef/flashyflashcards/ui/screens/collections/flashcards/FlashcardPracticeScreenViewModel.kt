package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.architecture.BaseViewModel
import cz.mendelu.pef.flashyflashcards.database.wordcollections.TestHistoryRepository
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsRepository
import cz.mendelu.pef.flashyflashcards.model.FlashcardAnswer
import cz.mendelu.pef.flashyflashcards.model.TestHistory
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.model.entities.TestHistoryEntity
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashcardPracticeScreenViewModel @Inject constructor(
    private val wordCollectionsRepository: WordCollectionsRepository,
    private val testHistoryRepository: TestHistoryRepository
) : BaseViewModel(), FlashcardPracticeScreenActions {

    var uiState by mutableStateOf(UiState<FlashcardPracticeScreenData, ScreenErrors>())
    private var testHistory: TestHistory? = null

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
                            val words = entity.wordEntities
                                .map { Word.createFromWordEntity(it) }
                                .shuffled()
                            val firstWord = words.firstOrNull()

                            uiState = UiState(
                                data = FlashcardPracticeScreenData(
                                    flashcardText = firstWord?.name ?: "",
                                    words = words
                                )
                            )
                        }
                }
            }
        }
    }

    override fun isAnswerCorrect(answer: String): Boolean {
        uiState.data?.let { data ->
            val processedAnswer = answer
                .lowercase()
                .trim()

            val correctAnswer = data.words[data.currentWordNumber].translation
                .lowercase()
                .trim()

            data.error = if (processedAnswer != correctAnswer) {
                R.string.incorrect_answer
            } else {
                null
            }

            uiState = UiState(
                data = uiState.data
            )

            return data.error == null
        }

        return false
    }

    override fun setNextWord() {
        uiState.data?.let { data ->
            val next = data.currentWordNumber + 1
            val wordsSize = data.words.size

            if (next != wordsSize) {
                data.answer = ""
                data.currentWordNumber = next
                data.flashcardText = data.words[next].name
            } else {
                // Each word was in a flashcard, so finish practising.
                data.finish = true
                testHistory?.dateOfCompletion = System.currentTimeMillis()
            }

            uiState = UiState(data = data)
        }
    }

    override fun setAnswer(answer: String) {
        uiState.data?.let { data ->
            data.answer = answer
        }

        uiState = UiState(
            data = uiState.data,
            errors = uiState.errors
        )
    }

    override fun setFlashcardText() {
        uiState.data?.let { data ->
            val currentWord = data.words[data.currentWordNumber]

            data.flashcardText = if (data.flashcardText == currentWord.name) {
                currentWord.translation
            } else {
                currentWord.name
            }
        }

        uiState = UiState(
            data = uiState.data,
            errors = uiState.errors
        )
    }

    override fun resetFlashcard() {
        uiState.data?.let { data ->
            data.answer = ""
            data.currentWordNumber = 0
            data.words = data.words.shuffled()
            data.flashcardText = data.words[0].name
            data.finish = false
        }

        if (testHistory != null) {
            testHistory = testHistory?.copy(
                answers = mutableListOf(),
                timeTaken = 0L,
                dateOfCompletion = 0L
            )
        }

        uiState = UiState(
            data = uiState.data
        )
    }

    override fun setTestHistory(collectionId: Long?) {
        testHistory = TestHistory(wordCollectionId = collectionId)
    }

    override fun updateTestHistory(flashcardAnswer: FlashcardAnswer, timeTaken: Long) {
        testHistory?.let { history ->
            history.timeTaken += timeTaken

            if (flashcardAnswer.answer.trim().lowercase() == flashcardAnswer.word.translation.trim().lowercase()) {
                history.numberOfCorrectAnswers += 1
            }

            history.answers.add(flashcardAnswer)
        }
    }

    override fun saveTestHistory() {
        testHistory?.let { history ->
            val testHistoryEntity = TestHistoryEntity.createFromTestHistory(history)

            launch {
                testHistoryRepository.createNewTestHistory(testHistoryEntity)
            }

            setTestHistory(history.wordCollectionId)
        }
    }

    override fun getTestHistory(): TestHistory? {
        return testHistory
    }
}