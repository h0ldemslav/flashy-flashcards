package cz.mendelu.pef.flashyflashcards.ui.screens.collections

import cz.mendelu.pef.flashyflashcards.model.Word

interface AddEditWordScreenActions {

    fun isWordValid(word: Word): Boolean
    fun setWord(word: Word?, collectionId: Long? = null)
    fun saveWord(word: Word)
    fun deleteWord(word: Word)
    fun translateWord(word: Word)
    fun closeTranslator()
}