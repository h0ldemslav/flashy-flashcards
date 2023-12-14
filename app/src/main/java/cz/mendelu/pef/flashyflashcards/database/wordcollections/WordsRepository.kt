package cz.mendelu.pef.flashyflashcards.database.wordcollections

import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.model.WordEntity
import kotlinx.coroutines.flow.Flow

interface WordsRepository {

    fun getAllWords(): Flow<List<WordEntity>>
    suspend fun addNewWord(word: Word)
    suspend fun updateWord(word: Word)
    suspend fun deleteWord(word: Word)
}