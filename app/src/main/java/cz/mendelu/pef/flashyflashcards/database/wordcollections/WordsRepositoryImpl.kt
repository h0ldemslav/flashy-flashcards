package cz.mendelu.pef.flashyflashcards.database.wordcollections

import cz.mendelu.pef.flashyflashcards.model.Word
import cz.mendelu.pef.flashyflashcards.model.WordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(
    private val wordsDao: WordsDao
) : WordsRepository {
    override fun getAllWords(): Flow<List<WordEntity>> {
        return wordsDao.getAllWords()
    }

    override suspend fun addNewWord(word: Word) {
        val wordEntity = WordEntity.createFromWord(word)
        wordsDao.addNewWord(wordEntity)
    }

    override suspend fun updateWord(word: Word) {
        val wordEntity = WordEntity.createFromWord(word)
        wordsDao.updateWord(wordEntity)
    }

    override suspend fun deleteWord(word: Word) {
        val wordEntity = WordEntity.createFromWord(word)
        wordsDao.deleteWord(wordEntity)
    }
}