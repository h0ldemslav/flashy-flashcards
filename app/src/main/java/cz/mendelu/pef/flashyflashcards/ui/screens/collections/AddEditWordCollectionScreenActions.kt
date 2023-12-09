package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import cz.mendelu.pef.flashyflashcards.model.WordCollection

interface AddEditWordCollectionScreenActions {

    fun getAllLanguages(): Map<String, String>
    fun createOrUpdateWordCollection(wordCollection: WordCollection?)
}