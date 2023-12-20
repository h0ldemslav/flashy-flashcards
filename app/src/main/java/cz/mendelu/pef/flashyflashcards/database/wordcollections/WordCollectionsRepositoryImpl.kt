package cz.mendelu.pef.flashyflashcards.database.wordcollections

import cz.mendelu.pef.flashyflashcards.model.WordCollection
import cz.mendelu.pef.flashyflashcards.model.entities.WordCollectionEntity
import cz.mendelu.pef.flashyflashcards.model.entities.WordCollectionEntityWithWordEntities
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordCollectionsRepositoryImpl @Inject constructor(
    private val wordCollectionsDao: WordCollectionsDao
) : WordCollectionsRepository {
    override fun getAllWordCollections(): Flow<List<WordCollectionEntity>> {
        return wordCollectionsDao.getAllWordCollections()
    }

    override fun getWordCollectionById(collectionId: Long?): Flow<WordCollectionEntity?> {
        return wordCollectionsDao.getWordCollectionById(collectionId)
    }

    override fun getWordCollectionAndWordsById(collectionId: Long?): Flow<WordCollectionEntityWithWordEntities?> {
        return wordCollectionsDao.getWordCollectionAndWordsById(collectionId)
    }

    override suspend fun createNewWordCollection(wordCollection: WordCollection) {
        val wordCollectionEntity = WordCollectionEntity.createFromCollection(wordCollection)

        wordCollectionsDao.createNewWordCollection(wordCollectionEntity)
    }

    override suspend fun updateWordCollection(wordCollection: WordCollection) {
        val wordCollectionEntity = WordCollectionEntity.createFromCollection(wordCollection)

        wordCollectionsDao.updateWordCollection(wordCollectionEntity)
    }

    override suspend fun deleteWordCollection(wordCollection: WordCollection) {
        val wordCollectionEntity = WordCollectionEntity.createFromCollection(wordCollection)

        wordCollectionsDao.deleteWordCollection(wordCollectionEntity)
    }
}