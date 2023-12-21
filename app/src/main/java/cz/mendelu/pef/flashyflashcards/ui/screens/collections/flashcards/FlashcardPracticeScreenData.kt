package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import cz.mendelu.pef.flashyflashcards.model.Word

data class FlashcardPracticeScreenData(
    var answer: String = "",
    var flashcardText: String = "",
    var currentWordNumber: Int = 0,
    var words: List<Word> = emptyList(),
    var finish: Boolean = false
)