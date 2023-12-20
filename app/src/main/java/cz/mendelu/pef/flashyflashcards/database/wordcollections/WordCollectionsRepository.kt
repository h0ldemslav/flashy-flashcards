package cz.mendelu.pef.flashyflashcards.database.wordcollections

import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.model.entities.WordCollectionEntity
import cz.mendelu.pef.flashyflashcards.model.entities.WordCollectionEntityWithWordEntities
import kotlinx.coroutines.flow.Flow

interface WordCollectionsRepository {

    fun getAllWordCollections(): Flow<List<WordCollectionEntity>>
    fun getWordCollectionById(collectionId: Long?): Flow<WordCollectionEntity?>
    fun getWordCollectionAndWordsById(collectionId: Long?): Flow<WordCollectionEntityWithWordEntities?>
    suspend fun createNewWordCollection(wordCollection: WordCollection)
    suspend fun updateWordCollection(wordCollection: WordCollection)
    suspend fun deleteWordCollection(wordCollection: WordCollection)
}