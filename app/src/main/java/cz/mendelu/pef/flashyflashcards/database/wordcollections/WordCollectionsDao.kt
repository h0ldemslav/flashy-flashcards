package cz.mendelu.pef.flashyflashcards.database.wordcollections

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import cz.mendelu.pef.flashyflashcards.model.entities.WordCollectionEntity
import cz.mendelu.pef.flashyflashcards.model.entities.WordCollectionEntityWithWordEntities
import kotlinx.coroutines.flow.Flow

@Dao
interface WordCollectionsDao {

    @Query("SELECT * FROM word_collections")
    fun getAllWordCollections(): Flow<List<WordCollectionEntity>>

    @Query("SELECT * FROM word_collections WHERE id=:collectionId")
    fun getWordCollectionById(collectionId: Long?): Flow<WordCollectionEntity?>

    @Transaction
    @Query("SELECT * FROM word_collections WHERE id=:collectionId")
    fun getWordCollectionAndWordsById(collectionId: Long?): Flow<WordCollectionEntityWithWordEntities?>

    @Insert
    suspend fun createNewWordCollection(collectionEntity: WordCollectionEntity)

    @Update
    suspend fun updateWordCollection(collectionEntity: WordCollectionEntity)

    @Delete
    suspend fun deleteWordCollection(collectionEntity: WordCollectionEntity)
}