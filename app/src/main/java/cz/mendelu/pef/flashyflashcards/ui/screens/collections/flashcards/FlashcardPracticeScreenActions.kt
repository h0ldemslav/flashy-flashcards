package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

interface FlashcardPracticeScreenActions {

    fun isAnswerCorrect(answer: String): Boolean
    fun setNextWord()
    fun setAnswer(answer: String)
    fun setFlashcardText()
    fun resetFlashcard()
}