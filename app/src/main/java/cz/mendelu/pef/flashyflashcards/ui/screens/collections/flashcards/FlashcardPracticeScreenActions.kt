package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import cz.mendelu.pef.flashyflashcards.model.FlashcardAnswer
import cz.mendelu.pef.flashyflashcards.model.TestHistory

interface FlashcardPracticeScreenActions {

    fun isAnswerCorrect(answer: String): Boolean
    fun setNextWord()
    fun setAnswer(answer: String)
    fun setFlashcardText()
    fun resetFlashcard()

    fun setTestHistory(collectionId: Long?)
    fun updateTestHistory(flashcardAnswer: FlashcardAnswer, timeTaken: Long)
    fun getTestHistory(): TestHistory?
    fun saveTestHistory()
}