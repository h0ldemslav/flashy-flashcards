package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

interface TrainingScreenActions {

    fun isAnswerCorrect(answer: String): Boolean
    fun setNextWord()
}