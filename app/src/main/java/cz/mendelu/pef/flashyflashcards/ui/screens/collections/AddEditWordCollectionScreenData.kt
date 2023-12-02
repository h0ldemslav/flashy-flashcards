package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import androidx.annotation.StringRes
import cz.mendelu.pef.flashyflashcards.model.WordCollection

data class AddEditWordCollectionScreenData(
    var wordCollection: WordCollection = WordCollection(
        id = null,
        name = "",
        sourceLanguage = "",
        targetLanguage = ""
    ),
    @StringRes
    var wordCollectionError: Int? = null
)