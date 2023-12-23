package cz.mendelu.pef.flashyflashcards.ui.screens.collections.flashcards

import androidx.annotation.StringRes
import cz.mendelu.pef.flashyflashcards.model.Word

data class FlashcardPracticeScreenData(
    var answer: String = "",
    var flashcardText: String = "",
    var currentWordNumber: Int = 0,
    var words: List<Word> = emptyList(),
    @StringRes
    var error: Int? = null,
    var finish: Boolean = false
)