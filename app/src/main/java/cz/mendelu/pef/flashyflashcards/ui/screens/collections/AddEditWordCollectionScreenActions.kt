package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import cz.mendelu.pef.flashyflashcards.model.WordCollection

interface AddEditWordCollectionScreenActions {

    fun getAllLanguages(): Map<String, String>
    fun isWordCollectionValid(wordCollection: WordCollection): Boolean
    fun setWordCollection(wordCollection: WordCollection?)
    fun saveWordCollection(wordCollection: WordCollection)
    fun deleteWordCollection(wordCollection: WordCollection)
}